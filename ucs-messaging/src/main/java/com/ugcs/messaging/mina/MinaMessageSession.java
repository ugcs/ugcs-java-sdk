package com.ugcs.messaging.mina;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ugcs.messaging.api.CloseListener;
import com.ugcs.messaging.api.MessageEvent;
import com.ugcs.messaging.api.MessageListener;
import com.ugcs.messaging.api.MessageSelector;
import com.ugcs.messaging.api.MessageSession;
import com.ugcs.messaging.api.MessageSessionErrorEvent;
import com.ugcs.messaging.api.MessageSessionEvent;

public class MinaMessageSession implements MessageSession {

	private static final Logger log = LoggerFactory.getLogger(MinaMessageSession.class);

	private final IoSession session;
	private final Map<MessageListener, MessageSelector> listeners = new HashMap<>();

	private static final MessageSelector SELECT_ALL = new MessageSelector() {
		public boolean select(Object message) {
			return true;
		}
	};

	public MinaMessageSession(IoSession session) {
		if (session == null)
			throw new IllegalArgumentException("session");

		this.session = session;
	}

	@Override
	public SocketAddress getLocalAddress() {
		return session == null
				? null
				: session.getLocalAddress();
	}

	@Override
	public SocketAddress getRemoteAddress() {
		return session == null
				? null
				: session.getRemoteAddress();
	}

	@Override
	public void addListener(MessageListener listener) {
		addListener(listener, SELECT_ALL);
	}

	@Override
	public void addListener(MessageListener listener, MessageSelector selector) {
		Objects.requireNonNull(listener);
		Objects.requireNonNull(selector);

		synchronized (listeners) {
			listeners.put(listener, selector);
		}
	}

	@Override
	public void removeListener(MessageListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public <T> void setAttribute(Object key, T value) {
		session.setAttribute(key, value);
	}

	@Override
	public <T> T getAttribute(Object key) {
		return (T)session.getAttribute(key);
	}

	@Override
	public <T> T getAttribute(Object key, T defaultValue) {
		return (T)session.getAttribute(key, defaultValue);
	}

	@Override
	public <T> T getAttribute(Object key, Supplier<T> supplier) {
		T value = (T)session.getAttribute(key);
		if (value == null) {
			synchronized (session) {
				value = (T)session.getAttribute(key);
				if (value == null) {
					value = supplier.get();
					session.setAttribute(key, value);
				}
			}
		}
		return value;
	}

	@Override
	public boolean isOpened() {
		return session.isConnected() && !session.isClosing();
	}

	@Override
	public void close() throws IOException {
		CloseFuture closeFuture = session.close(false);
		closeFuture.awaitUninterruptibly();
		if (!closeFuture.isClosed())
			throw new IOException("Session not closed");
	}

	@Override
	public void closeNonBlocking(final CloseListener listener) {
		CloseFuture closeFuture = session.close(false);
		if (listener != null) {
			closeFuture.addListener(new IoFutureListener<CloseFuture>() {
				@Override
				public void operationComplete(CloseFuture future) {
					if (future == null)
						throw new IllegalArgumentException("future");

					if (!future.isClosed()) {
						MessageSessionErrorEvent event = new MessageSessionErrorEvent(
								MinaMessageSession.this,
								MinaMessageSession.this,
								new IOException("Session not closed"));
						listener.closeError(event);
						return;
					}
					MessageSessionEvent event = new MessageSessionEvent(
							MinaMessageSession.this,
							MinaMessageSession.this);
					listener.closed(event);
				}
			});
		}
	}

	@Override
	public Future<Void> send(Object message) {
		Objects.requireNonNull(message);

		return new WriteFutureAdapter(session.write(message));
	}

	protected void messageReceived(Object message) throws Exception {
		List<MessageListener> listenersCopy = new ArrayList<>();
		synchronized (listeners) {
			for (Map.Entry<MessageListener, MessageSelector> entry : listeners.entrySet()) {
				MessageSelector selector = entry.getValue();
				if (selector == null || selector.select(message))
					listenersCopy.add(entry.getKey());
			}
		}
		if (listenersCopy.size() == 0) {
			log.warn("No listener registered for message, message skipped");
			return;
		}
		MessageEvent messageEvent = new MessageEvent(this, this, message);
		for (MessageListener listener : listenersCopy) {
			try {
				listener.messageReceived(messageEvent);
			} catch (Throwable ignore) {
				// continue to the next listener
				log.warn("Listener error", ignore);
			}
		}
	}

	protected void cancelAllListeners() {
		List<MessageListener> listenersCopy = new ArrayList<>();
		synchronized (listeners) {
			listenersCopy.addAll(listeners.keySet());
		}
		for (MessageListener listener : listenersCopy) {
			try {
				listener.cancelled();
			} catch (Throwable ignore) {
				// continue to the next listener
				log.warn("Listener cancellation error", ignore);
			}
		}
	}

	@Override
	public String toString() {
		return new StringBuilder("{id: ")
				.append(Long.toString(session.getId()))
				.append(", local: ")
				.append(session.getLocalAddress())
				.append(", remote: ")
				.append(session.getRemoteAddress())
				.append("}")
				.toString();
	}
}
