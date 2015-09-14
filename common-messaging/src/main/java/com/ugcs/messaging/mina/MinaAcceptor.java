package com.ugcs.messaging.mina;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ugcs.messaging.api.Acceptor;
import com.ugcs.messaging.api.CodecFactory;
import com.ugcs.messaging.api.MessageSessionListener;

public class MinaAcceptor implements Acceptor {
	private static final Logger log = LoggerFactory.getLogger(MinaAcceptor.class);
	
	private static final int DEFAULT_IO_PROCESSORS = 4;
	private static final int DEFAULT_THREAD_POOL_SIZE = 16;
	private static final int DEFAULT_SESSION_IDLE_SECONDS = 300;
	
	private final SocketAcceptor acceptor;
	private final ExecutorService executorService;
	private final MinaAdapter minaAdapter;
	
	public MinaAcceptor(CodecFactory codecFactory, int ioProcessors, int threadPoolSize) {
		this(new MinaCodecFactory(codecFactory), ioProcessors, threadPoolSize);
	}
	
	public MinaAcceptor(ProtocolCodecFactory codecFactory, int ioProcessors, int threadPoolSize) {
		if (codecFactory == null)
			throw new IllegalArgumentException("codecFactory");
		
		log.debug("Initializing acceptor {I/O processors: {}, pool size: {}}", 
				ioProcessors, threadPoolSize);
		
		this.acceptor = new NioSocketAcceptor(ioProcessors);
		this.executorService = Executors.newFixedThreadPool(threadPoolSize);

		this.acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(codecFactory));
		this.acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(executorService));
		this.acceptor.getFilterChain().addLast("logger", new LoggingFilter());

		// session handler
		this.minaAdapter = new MinaAdapter();
		this.acceptor.setHandler(minaAdapter);
		
		// acceptor configuration
		// disable disconnection on unbind
		this.acceptor.setCloseOnDeactivation(false);
		// port reuse when socket is in TIME_WAIT state
		this.acceptor.setReuseAddress(true);
		this.acceptor.getSessionConfig().setReuseAddress(true);
		this.acceptor.getSessionConfig().setKeepAlive(true);
		this.acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, DEFAULT_SESSION_IDLE_SECONDS);
		// no Nagle's algorithm
		this.acceptor.getSessionConfig().setTcpNoDelay(true);
	}
	
	public MinaAcceptor(ProtocolCodecFactory codecFactory) {
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
	
	public void start(SocketAddress socketAddress) throws IOException {
		acceptor.bind(socketAddress);
	}
	
	public void stop() {
		acceptor.unbind();
	}
	
	public void close() {
		for (IoSession session : acceptor.getManagedSessions().values())
			session.close(false);
		// disposing selector resources
		acceptor.dispose();
		// stopping thread poll tasks
		executorService.shutdown();
	}
}
