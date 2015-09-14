package com.ugcs.messaging.mina;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ugcs.messaging.api.CodecFactory;
import com.ugcs.messaging.api.ConnectionListener;
import com.ugcs.messaging.api.Connector;
import com.ugcs.messaging.api.MessageSession;
import com.ugcs.messaging.api.MessageSessionErrorEvent;
import com.ugcs.messaging.api.MessageSessionEvent;
import com.ugcs.messaging.api.MessageSessionListener;

public class MinaConnector implements Connector {
	private static final Logger log = LoggerFactory.getLogger(MinaConnector.class);
	
	private static final int DEFAULT_IO_PROCESSORS = 16;
	private static final int DEFAULT_THREAD_POOL_SIZE = 32;
	private static final int DEFAULT_SESSION_IDLE_SECONDS = 300;
	
	private final SocketConnector connector;
	private final ExecutorService executorService;
	private final MinaAdapter minaAdapter;
	
	public MinaConnector(CodecFactory codecFactory, int ioProcessors, int threadPoolSize) {
		this(new MinaCodecFactory(codecFactory), ioProcessors, threadPoolSize);
	}
	
	public MinaConnector(CodecFactory codecFactory) {
		this(new MinaCodecFactory(codecFactory), DEFAULT_IO_PROCESSORS, DEFAULT_THREAD_POOL_SIZE);
	}
	
	public MinaConnector(ProtocolCodecFactory codecFactory, int ioProcessors, int threadPoolSize) {
		if (codecFactory == null)
			throw new IllegalArgumentException("codecFactory");
		
		log.debug("Initializing connector {I/O processors: {}, pool size: {}}", 
				Integer.toString(ioProcessors),
				threadPoolSize > 0 ? Integer.toString(threadPoolSize) : "unbounded");

		this.connector = new NioSocketConnector(ioProcessors);
		this.executorService = threadPoolSize > 0 ?
					Executors.newFixedThreadPool(threadPoolSize) :
					Executors.newCachedThreadPool();
		
		this.connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(codecFactory));
		this.connector.getFilterChain().addLast("threadPool", new ExecutorFilter(executorService));
		this.connector.getFilterChain().addLast("logger", new LoggingFilter());

		// session handler
		this.minaAdapter = new MinaAdapter();
		this.connector.setHandler(minaAdapter);
		
		// connector configuration
		this.connector.getSessionConfig().setReuseAddress(true);
		this.connector.getSessionConfig().setKeepAlive(true);
		this.connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, DEFAULT_SESSION_IDLE_SECONDS);
		// no Nagle's algorithm
		this.connector.getSessionConfig().setTcpNoDelay(true);
	}
	
	public MinaConnector(ProtocolCodecFactory codecFactory) {
		this(codecFactory, DEFAULT_IO_PROCESSORS, DEFAULT_THREAD_POOL_SIZE);
	}
	
	public void addSessionListener(MessageSessionListener sessionListener) {
		if (sessionListener == null)
			throw new IllegalArgumentException("sessionListener");
		
		minaAdapter.addSessionListener(sessionListener);
	}
	
	public void removeSessionListener(MessageSessionListener sessionListener) {
		if (sessionListener == null)
			throw new IllegalArgumentException("sessionListener");
		
		minaAdapter.removeSessionListener(sessionListener);
	}
	
	public MessageSession connect(SocketAddress address) throws IOException {
		if (address == null)
			throw new IllegalArgumentException("address");
		
		// client session
		ConnectFuture connectFuture = connector.connect(address);
		connectFuture.awaitUninterruptibly();
		if (!connectFuture.isConnected() || connectFuture.getException() != null)
			throw new IOException("Connection error", connectFuture.getException());
		
		IoSession minaSession = connectFuture.getSession();
		return minaAdapter.getMessageSession(minaSession);
	}

	public void connectNonBlocking(SocketAddress address, final ConnectionListener listener) {
		if (address == null)
			throw new IllegalArgumentException("address");
		
		ConnectFuture connectFuture = connector.connect(address);
		if (listener != null) {
			connectFuture.addListener(new IoFutureListener<ConnectFuture>() {
				@Override
				public void operationComplete(ConnectFuture future) {
					if (future == null)
						throw new IllegalArgumentException("future");
					
					// connect future status
					if (!future.isConnected() || future.getException() != null) {
						MessageSessionErrorEvent event = new MessageSessionErrorEvent(
								MinaConnector.this,
								null,
								future.getException());
						listener.connectionError(event);
						return;
					}
					
					// mina session
					try {
						IoSession minaSession = future.getSession();
						MessageSessionEvent event = new MessageSessionEvent(
								MinaConnector.this,
								minaAdapter.getMessageSession(minaSession));
						listener.connected(event);
					} catch (Throwable e) {
						MessageSessionErrorEvent event = new MessageSessionErrorEvent(
								MinaConnector.this,
								null,
								e);
						listener.connectionError(event);
					}
				}});
		}
	}

	public void close() {
		// closing underlying session
		for (IoSession session : connector.getManagedSessions().values())
			session.close(false);
		// disposing selector resources
		connector.dispose();
		// stopping thread poll tasks
		executorService.shutdown();
	}
}
