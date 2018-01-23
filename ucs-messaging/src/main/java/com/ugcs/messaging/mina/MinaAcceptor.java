package com.ugcs.messaging.mina;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ugcs.messaging.GroupingThreadPool;
import com.ugcs.messaging.TaskMapper;
import com.ugcs.messaging.api.Acceptor;
import com.ugcs.messaging.api.CodecFactory;
import com.ugcs.messaging.api.MessageSessionListener;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaAcceptor implements Acceptor {

	private static final Logger log = LoggerFactory.getLogger(MinaAcceptor.class);

	private static final int DEFAULT_MAX_IO_THREADS = 4;
	private static final int DEFAULT_MAX_TASK_THREADS = 32;
	private static final int DEFAULT_SESSION_IDLE_SECONDS = 3;

	private final SocketAcceptor acceptor;
	private final MinaAdapter minaAdapter;
	private final ExecutorService executor;

	public MinaAcceptor(CodecFactory codecFactory) {
		this(codecFactory, DEFAULT_MAX_IO_THREADS, DEFAULT_MAX_TASK_THREADS);
	}

	public MinaAcceptor(CodecFactory codecFactory, int maxIoThreads, int maxTaskThreads) {
		this(codecFactory, maxIoThreads, maxTaskThreads, MinaTaskMappers.orderedByMessageTypes());
	}

	public MinaAcceptor(CodecFactory codecFactory, int maxIoThreads, int maxTaskThreads, TaskMapper taskMapper) {
		this(codecFactory, new SimpleIoProcessorPool<>(NioProcessor.class, maxIoThreads),
				newExecutor(maxTaskThreads, taskMapper));
		log.info("Initialized acceptor {max I/O threads: {}, max task threads: {}}",
				maxIoThreads,
				maxTaskThreads > 0
						? Integer.toString(maxTaskThreads)
						: "unbounded");
	}

	public MinaAcceptor(CodecFactory codecFactory, IoProcessor<NioSession> processor, ExecutorService executor) {
		Objects.requireNonNull(codecFactory);
		Objects.requireNonNull(processor);
		Objects.requireNonNull(executor);

		acceptor = new NioSocketAcceptor(processor);
		DefaultIoFilterChainBuilder filters = acceptor.getFilterChain();

		// encoding
		filters.addLast("codec", new ProtocolCodecFilter(new MinaCodecFactory(codecFactory)));

		// thread model
		this.executor = executor;
		filters.addLast("threadPool", new ExecutorFilter(executor));

		// logging
		filters.addLast("logger", new LoggingFilter());

		// session handler
		minaAdapter = new MinaAdapter();
		acceptor.setHandler(minaAdapter);

		// acceptor configuration
		// disable disconnection on unbind
		acceptor.setCloseOnDeactivation(false);
		// port reuse when socket is in TIME_WAIT state
		acceptor.setReuseAddress(true);
		acceptor.getSessionConfig().setReuseAddress(true);
		acceptor.getSessionConfig().setKeepAlive(true);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, DEFAULT_SESSION_IDLE_SECONDS);
		// no Nagle's algorithm
		acceptor.getSessionConfig().setTcpNoDelay(true);
	}

	private static ExecutorService newExecutor(int maxThreads, TaskMapper taskMapper) {
		if (taskMapper == null) {
			// unspecified order
			// maxThreads = 0 -> unbound pool
			return maxThreads > 0
					? Executors.newFixedThreadPool(maxThreads)
					: Executors.newCachedThreadPool();
		} else {
			// tasks are ordered within groups
			maxThreads = Math.max(1, maxThreads);
			int coreThreads = Math.max(1, maxThreads / 2);
			return new GroupingThreadPool(coreThreads, maxThreads, taskMapper);
		}
	}

	public void addSessionListener(MessageSessionListener sessionListener) {
		Objects.requireNonNull(sessionListener);
		minaAdapter.addSessionListener(sessionListener);
	}

	public void removeSessionListener(MessageSessionListener sessionListener) {
		Objects.requireNonNull(sessionListener);
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
		// stopping executor service
		executor.shutdown();
	}
}
