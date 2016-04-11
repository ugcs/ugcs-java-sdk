package com.ugcs.messaging.mina;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
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
	private static final int DEFAULT_THREAD_POOL_SIZE = 32;
	private static final int DEFAULT_SESSION_IDLE_SECONDS = 300;
	
	private final SocketAcceptor acceptor;
	private final MinaAdapter minaAdapter;
	private final List<ExecutorService> executors = new ArrayList<>();

	public MinaAcceptor(CodecFactory codecFactory) {
		this(codecFactory, DEFAULT_IO_PROCESSORS, DEFAULT_THREAD_POOL_SIZE, false, false);
	}

	public MinaAcceptor(CodecFactory codecFactory, int ioProcessors, int threadPoolSize) {
		this(codecFactory, ioProcessors, threadPoolSize, false, false);
	}

	public MinaAcceptor(CodecFactory codecFactory, int ioProcessors, int threadPoolSize,
			boolean orderedRead, boolean orderedWrite) {
		if (codecFactory == null)
			throw new IllegalArgumentException("codecFactory");
		
		log.info("Initializing acceptor {I/O processors: {}, pool size: {}, ordered R/W: {}/{}}", 
				ioProcessors,
				threadPoolSize > 0 ? Integer.toString(threadPoolSize) : "unbounded",
				orderedRead,
				orderedWrite);
		
		this.acceptor = new NioSocketAcceptor(ioProcessors);
		DefaultIoFilterChainBuilder filters = this.acceptor.getFilterChain();

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
		// stopping executor services
		for (ExecutorService executor : executors)
			executor.shutdown();
	}
}
