package com.ugcs.messaging.mina;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ugcs.messaging.api.CodecFactory;
import com.ugcs.messaging.api.ConnectListener;
import com.ugcs.messaging.api.Connector;
import com.ugcs.messaging.api.MessageSession;
import com.ugcs.messaging.api.MessageSessionErrorEvent;
import com.ugcs.messaging.api.MessageSessionEvent;
import com.ugcs.messaging.api.MessageSessionListener;

public class MinaConnector implements Connector {
	private static final Logger log = LoggerFactory.getLogger(MinaConnector.class);
	
	private static final int DEFAULT_IO_PROCESSORS = 4;
	private static final int DEFAULT_THREAD_POOL_SIZE = 16;
	private static final int DEFAULT_SESSION_IDLE_SECONDS = 300;
	
	private final SocketConnector connector;
	private final MinaAdapter minaAdapter;
	private final List<ExecutorService> executors = new ArrayList<>();
	
	public MinaConnector(CodecFactory codecFactory) {
		this(codecFactory, DEFAULT_IO_PROCESSORS, DEFAULT_THREAD_POOL_SIZE, false, false);
	}

	public MinaConnector(CodecFactory codecFactory, int ioProcessors, int threadPoolSize) {
		this(codecFactory, ioProcessors, threadPoolSize, false, false);
	}
	
	public MinaConnector(CodecFactory codecFactory, int ioProcessors, int threadPoolSize, 
			boolean orderedRead, boolean orderedWrite) {
		if (codecFactory == null)
			throw new IllegalArgumentException("codecFactory");
		
		log.info("Initializing connector {I/O processors: {}, pool size: {}, ordered R/W: {}/{}}", 
				Integer.toString(ioProcessors),
				threadPoolSize > 0 ? Integer.toString(threadPoolSize) : "unbounded",
				orderedRead,
				orderedWrite);

		this.connector = new NioSocketConnector(ioProcessors);
		DefaultIoFilterChainBuilder filters = this.connector.getFilterChain();
		
		// encoding
		filters.addLast("codec", new ProtocolCodecFilter(new MinaCodecFactory(codecFactory)));
		
		// thread model
		if (orderedRead && orderedWrite) {
			ExecutorService executor = newOrderedExecutor(threadPoolSize);
			executors.add(executor);
			filters.addLast("threadPool-RW", new ExecutorFilter(executor));
		} else if (!orderedRead && !orderedWrite) {
			ExecutorService executor = newExecutor(threadPoolSize);
			executors.add(executor);
			filters.addLast("threadPool-RW", new ExecutorFilter(executor));
		} else {
			ExecutorService readExecutor = orderedRead
					? newOrderedExecutor(threadPoolSize)
					: newExecutor(threadPoolSize);
			executors.add(readExecutor);
			filters.addLast("threadPool-R", new ExecutorFilter(readExecutor));
			ExecutorService writeExecutor = orderedWrite
					? newOrderedExecutor(threadPoolSize)
					: newExecutor(threadPoolSize);
			executors.add(writeExecutor);
			filters.addLast("threadPool-W", new ExecutorFilter(writeExecutor, IoEventType.WRITE));
		}

		// logging
		filters.addLast("logger", new LoggingFilter());

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
	
	private ExecutorService newExecutor(int threadPoolSize) {
		return threadPoolSize > 0
				? Executors.newFixedThreadPool(threadPoolSize)
				: Executors.newCachedThreadPool();
	}
	
	private ExecutorService newOrderedExecutor(int threadPoolSize) {
		return new OrderedThreadPoolExecutor(
				0,
				threadPoolSize > 0 ? threadPoolSize : DEFAULT_THREAD_POOL_SIZE,
				30,
				TimeUnit.SECONDS,
				Executors.defaultThreadFactory(),
				null);
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

	public void connectNonBlocking(SocketAddress address, final ConnectListener listener) {
		if (address == null)
			throw new IllegalArgumentException("address");
		
		ConnectFuture connectFuture = connector.connect(address);
		if (listener != null) {
			connectFuture.addListener(new IoFutureListener<ConnectFuture>() {
				@Override
				public void operationComplete(ConnectFuture future) {
					if (future == null)
						throw new IllegalArgumentException("future");
					
					if (!future.isConnected() || future.getException() != null) {
						MessageSessionErrorEvent event = new MessageSessionErrorEvent(
								MinaConnector.this,
								null,
								future.getException());
						listener.connectError(event);
						return;
					}
					MessageSession messageSession = null;
					try {
						IoSession minaSession = future.getSession();
						messageSession = minaAdapter.getMessageSession(minaSession);
					} catch (Throwable e) {
						MessageSessionErrorEvent event = new MessageSessionErrorEvent(
								MinaConnector.this,
								null,
								e);
						listener.connectError(event);
						return;
					}
					MessageSessionEvent event = new MessageSessionEvent(
							MinaConnector.this,
							messageSession);
					listener.connected(event);
				}});
		}
	}

	public void close() {
		// closing underlying session
		for (IoSession session : connector.getManagedSessions().values())
			session.close(false);
		// disposing selector resources
		connector.dispose();
		// stopping executor services
		for (ExecutorService executor : executors)
			executor.shutdown();
	}
}
