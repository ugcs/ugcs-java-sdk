package com.ugcs.messaging.mina;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ugcs.messaging.GroupingThreadPool;
import com.ugcs.messaging.TaskMapper;
import com.ugcs.messaging.api.CodecFactory;
import com.ugcs.messaging.api.ConnectListener;
import com.ugcs.messaging.api.Connector;
import com.ugcs.messaging.api.MessageSession;
import com.ugcs.messaging.api.MessageSessionErrorEvent;
import com.ugcs.messaging.api.MessageSessionEvent;
import com.ugcs.messaging.api.MessageSessionListener;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaConnector implements Connector {

	private static final Logger log = LoggerFactory.getLogger(MinaConnector.class);

	private static final int DEFAULT_MAX_IO_THREADS = 4;
	private static final int DEFAULT_MAX_TASK_THREADS = 16;
	private static final int DEFAULT_SESSION_IDLE_SECONDS = 3;

	private final SocketConnector connector;
	private final MinaAdapter minaAdapter;
	private final ExecutorService executor;

	public MinaConnector(CodecFactory codecFactory) {
		this(codecFactory, DEFAULT_MAX_IO_THREADS, DEFAULT_MAX_TASK_THREADS);
	}

	public MinaConnector(CodecFactory codecFactory, int maxIoThreads, int maxTaskThreads) {
		this(codecFactory, maxIoThreads, maxTaskThreads, MinaTaskMappers.orderedByMessageTypes());
	}

	public MinaConnector(CodecFactory codecFactory, int maxIoThreads, int maxTaskThreads, TaskMapper taskMapper) {
		this(codecFactory, new SimpleIoProcessorPool<>(NioProcessor.class, maxIoThreads),
				newExecutor(maxTaskThreads, taskMapper));
		log.info("Initialized connector {max I/O threads: {}, max task threads: {}}",
				Integer.toString(maxIoThreads),
				maxTaskThreads > 0
						? Integer.toString(maxTaskThreads)
						: "unbounded");
	}

	public MinaConnector(CodecFactory codecFactory, IoProcessor<NioSession> processor, ExecutorService executor) {
		Objects.requireNonNull(codecFactory);
		Objects.requireNonNull(processor);
		Objects.requireNonNull(executor);

		connector = new NioSocketConnector(processor);
		DefaultIoFilterChainBuilder filters = connector.getFilterChain();

		// encoding
		filters.addLast("codec", new ProtocolCodecFilter(new MinaCodecFactory(codecFactory)));

		// thread model
		this.executor = executor;
		filters.addLast("threadPool", new ExecutorFilter(executor));

		// logging
		filters.addLast("logger", new LoggingFilter());

		// session handler
		minaAdapter = new MinaAdapter();
		connector.setHandler(minaAdapter);

		// connector configuration
		connector.getSessionConfig().setReuseAddress(true);
		connector.getSessionConfig().setKeepAlive(true);
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, DEFAULT_SESSION_IDLE_SECONDS);
		// no Nagle's algorithm
		connector.getSessionConfig().setTcpNoDelay(true);
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

	public MessageSession connect(SocketAddress address) throws IOException {
		Objects.requireNonNull(address);

		// client session
		ConnectFuture connectFuture = connector.connect(address);
		connectFuture.awaitUninterruptibly();
		if (!connectFuture.isConnected() || connectFuture.getException() != null)
			throw new IOException("Connection error", connectFuture.getException());

		IoSession minaSession = connectFuture.getSession();
		return minaAdapter.getMessageSession(minaSession);
	}

	public void connectNonBlocking(SocketAddress address, final ConnectListener listener) {
		Objects.requireNonNull(address);

		ConnectFuture connectFuture = connector.connect(address);
		if (listener != null) {
			connectFuture.addListener(new IoFutureListener<ConnectFuture>() {
				@Override
				public void operationComplete(ConnectFuture future) {
					Objects.requireNonNull(future);

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
				}
			});
		}
	}

	public void close() {
		for (IoSession session : connector.getManagedSessions().values())
			session.close(false);
		// disposing selector resources
		connector.dispose();
		// stopping executor service
		executor.shutdown();
	}
}
