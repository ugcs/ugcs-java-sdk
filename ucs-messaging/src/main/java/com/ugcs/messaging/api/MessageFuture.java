package com.ugcs.messaging.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.ugcs.messaging.AbstractListenableFuture;
import com.ugcs.messaging.CompletionEvent;
import com.ugcs.messaging.CompletionListener;

public class MessageFuture extends AbstractListenableFuture<Object> implements MessageListener {

	private final MessageSession session;
	private final List<CompletionListener<Object>> listeners = new ArrayList<>();

	private FutureState state;
	private FutureResult result;
	private final Object stateSync = new Object();

	public MessageFuture(MessageSession session, Object message, MessageSelector selector) {
		Objects.requireNonNull(session);
		Objects.requireNonNull(message);

		this.session = session;

		// sending message
		this.session.addListener(this, selector);
		try {
			// acquire state lock to make
			// it visible in message result handler
			synchronized (stateSync) {
				this.state = FutureState.RUNNING;
				this.session.send(message);
			}
		} catch (Throwable e) {
			this.session.removeListener(this);
			throw e;
		}
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		synchronized (stateSync) {
			if (state != FutureState.RUNNING || !mayInterruptIfRunning)
				return false;

			completeIfRunning(FutureState.CANCELLED, null, null);
			return state == FutureState.CANCELLED;
		}
	}

	@Override
	public boolean isCancelled() {
		synchronized (stateSync) {
			return state == FutureState.CANCELLED;
		}
	}

	@Override
	public boolean isDone() {
		// completion may be due to normal termination (success),
		// an exception (failure, timeout) or cancellation
		synchronized (stateSync) {
			return state == FutureState.SUCCEEDED
					|| state == FutureState.FAILED
					|| state == FutureState.CANCELLED;
		}
	}

	public Object get() throws InterruptedException, ExecutionException {
		synchronized (stateSync) {
			// wait while task is running
			while (state == FutureState.RUNNING) {
				// wait
				// interrupt does not cause task cancellation
				stateSync.wait();
			}
			// obtain result
			return getResult();
		}
	}

	public Object get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		if (timeout < 0)
			throw new IllegalArgumentException("Timeout must be non-negative");
		if (unit == null)
			throw new IllegalArgumentException("Time unit not specified");

		long t = System.nanoTime() + unit.toNanos(timeout);
		synchronized (stateSync) {
			// wait while task is running
			while (state == FutureState.RUNNING) {
				long nanosTimeout = t - System.nanoTime();
				// timeout check
				if (nanosTimeout <= 0) {
					// fail with timeout if task is still running
					// after waiting
					if (state == FutureState.RUNNING)
						throw new TimeoutException();
				}
				// wait
				// interrupt does not cause task cancellation
				stateSync.wait(
						nanosTimeout / 1_000_000L,
						(int)(nanosTimeout % 1_000_000L));
			}
			// obtain result
			return getResult();
		}
	}

	private void completeIfRunning(FutureState state, Object value, Throwable error) {
		if (state == null || state == FutureState.RUNNING)
			throw new IllegalArgumentException("state");

		boolean completed = false;
		List<CompletionListener<Object>> listenersCopy = null;
		try {
			synchronized (stateSync) {
				if (this.state == FutureState.RUNNING) {
					this.state = state;
					this.result = new FutureResult(value, error);
					completed = true;

					// copy completion listeners
					listenersCopy = new ArrayList<>(listeners);
					listeners.clear();

					// notify all waiting threads
					stateSync.notifyAll();
				}
			}
		} finally {
			if (completed) {
				session.removeListener(this);

				// fire completed event
				invokeCompleted(listenersCopy);
			}
		}
	}

	private Object getResult() throws ExecutionException {
		synchronized (stateSync) {
			switch (state) {
				case SUCCEEDED:
					return result.getValue();
				case FAILED:
					throwResultException();
					break;
				case CANCELLED:
					throw new CancellationException();
				default:
					break;
			}
		}
		throw new IllegalStateException();
	}

	private void throwResultException() throws ExecutionException {
		try {
			throw result.getError();
		} catch (ExecutionException e) {
			throw e;
		} catch (Throwable e) {
			throw new ExecutionException(e);
		}
	}

	/* MessageListener implementation */

	@Override
	public void messageReceived(MessageEvent event) {
		Objects.requireNonNull(event);

		completeIfRunning(FutureState.SUCCEEDED, event.getMessage(), null);
	}

	@Override
	public void cancelled() {
		cancel(true);
	}
	
	/* inner classes */

	enum FutureState {
		RUNNING,
		CANCELLED,
		FAILED,
		SUCCEEDED,
	}

	static class FutureResult {

		private final Object value;
		private final Throwable error;

		FutureResult(Object value, Throwable error) {
			this.value = value;
			this.error = error;
		}

		Object getValue() {
			return value;
		}

		Throwable getError() {
			return error;
		}
	}

	/* events */

	@Override
	public void addCompletionListener(CompletionListener<Object> listener) {
		Objects.requireNonNull(listener);

		boolean invokeNow = true;
		synchronized (stateSync) {
			if (state == FutureState.RUNNING) {
				listeners.add(listener);
				invokeNow = false;
			}
		}
		if (invokeNow) {
			// fire completion event for this listener
			invokeCompleted(Collections.singletonList(listener));
		}
	}

	private void invokeCompleted(List<CompletionListener<Object>> listeners) {
		if (listeners == null || listeners.isEmpty())
			return;

		Object value = null;
		Throwable error = null;

		synchronized (stateSync) {
			if (state == FutureState.RUNNING)
				throw new IllegalStateException();

			value = state == FutureState.SUCCEEDED
					? result.getValue()
					: null;
			error = state == FutureState.FAILED
					? result.getError()
					: state == FutureState.CANCELLED
							? new CancellationException()
							: null;
		}

		CompletionEvent<Object> event = new CompletionEvent<>(this, value, error);
		for (CompletionListener<Object> listener : listeners) {
			try {
				listener.completed(event);
			} catch (Exception ignore) {
			}
		}
	}
}
