package com.ugcs.messaging.api;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.ugcs.messaging.AbstractListenableFuture;
import com.ugcs.messaging.CompletionEvent;
import com.ugcs.messaging.CompletionListener;

public class MessageFuture extends AbstractListenableFuture<Object> implements MessageListener {

	private final MessageSession session;
	private final Queue<CompletionListener<Object>> listeners = new ArrayDeque<>();

	private FutureState state;
	private FutureResult result;
	private boolean draining;
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
	public void addCompletionListener(CompletionListener<Object> listener) {
		Objects.requireNonNull(listener);

		boolean invokeNow;
		synchronized (stateSync) {
			listeners.offer(listener);
			invokeNow = state != FutureState.RUNNING;
		}
		if (invokeNow) {
			// drain completion queue
			drainCompletionListeners();
		}
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		boolean cancelled;
		synchronized (stateSync) {
			if (state != FutureState.RUNNING || !mayInterruptIfRunning)
				return false;
			cancelled = completeIfRunning(FutureState.CANCELLED, null, null);
		}
		// assert: if cancelled => state == FutureState.CANCELLED
		if (cancelled) {
			drainCompletionListeners();
		}
		return cancelled;
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

	// listeners still can be added after calling completion await
	// in that case add listeners/await completion calls should be
	// synchronized externally
	@Override
	public void awaitCompletionListeners(long timeout, TimeUnit unit)
			throws InterruptedException, TimeoutException {
		if (timeout < 0)
			throw new IllegalArgumentException("Timeout must be non-negative");
		if (unit == null)
			throw new IllegalArgumentException("Time unit not specified");

		long t = System.nanoTime() + unit.toNanos(timeout);
		synchronized (stateSync) {
			// wait while listeners queue is not empty
			while (!listeners.isEmpty()) {
				long nanosTimeout = t - System.nanoTime();
				// timeout check
				if (nanosTimeout <= 0) {
					// fail with timeout if listeners still not invoked
					if (!listeners.isEmpty())
						throw new TimeoutException();
				}
				// wait
				// interrupt does not cause listeners cancellation
				stateSync.wait(
						nanosTimeout / 1_000_000L,
						(int)(nanosTimeout % 1_000_000L));
			}
		}
	}

	// returns true if future was completed on this call
	private boolean completeIfRunning(FutureState state, Object value, Throwable error) {
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

					// notify all waiting threads
					stateSync.notifyAll();
				}
			}
		} finally {
			if (completed) {
				session.removeListener(this);
			}
		}
		return completed;
	}

	private void drainCompletionListeners() {
		synchronized (stateSync) {
			if (state == FutureState.RUNNING)
				throw new IllegalStateException();
			// guard from processing queue by multiple threads
			// not necessary, but should be more predictive
			if (draining)
				return;
			draining = true;
		}
		try {
			while (true) {
				CompletionListener<Object> listener;
				CompletionEvent<Object> event;
				synchronized (stateSync) {
					listener = listeners.poll();
					if (listener == null) {
						draining = false;
						// notify all waiting threads and exit loop
						stateSync.notifyAll();
						break;
					}

					// build completion event from current state
					Object value = state == FutureState.SUCCEEDED
							? result.getValue()
							: null;
					Throwable error = state == FutureState.FAILED
							? result.getError()
							: state == FutureState.CANCELLED
							? new CancellationException()
							: null;
					event = new CompletionEvent<>(this, value, error);
				}

				// assert: listener != null && event != null
				try {
					listener.completed(event);
				} catch (Exception ignore) {
				}
			}
		} catch (Exception e) {
			synchronized (stateSync) {
				draining = false;
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

	@Override
	public void messageReceived(MessageEvent event) {
		Objects.requireNonNull(event);

		boolean completed = completeIfRunning(FutureState.SUCCEEDED, event.getMessage(), null);
		if (completed) {
			drainCompletionListeners();
		}
	}

	@Override
	public void cancelled() {
		cancel(true);
	}

	enum FutureState {
		RUNNING,
		CANCELLED,
		FAILED,
		SUCCEEDED
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
}
