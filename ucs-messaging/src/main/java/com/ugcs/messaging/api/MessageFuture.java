package com.ugcs.messaging.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MessageFuture implements Future<Object>, MessageListener {
	private final MessageSession session;
	private final List<EventListener> listeners = new ArrayList<>();
	
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
			return
					state == FutureState.SUCCEEDED || 
					state == FutureState.FAILED || 
					state == FutureState.CANCELLED;
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
						(int) (nanosTimeout % 1_000_000L));
			}
			// obtain result
			return getResult();
		}
	}
	
	private void completeIfRunning(FutureState state, Object value, Throwable error) {
		if (state == null || state == FutureState.RUNNING)
			throw new IllegalArgumentException("state");

		boolean completed = false;
		List<CompletionListener> completionListeners = null;
		try {
			synchronized (stateSync) {
				if (this.state == FutureState.RUNNING) {
					this.state = state;
					this.result = new FutureResult(value, error);
					completed = true;

					// copy completion listeners
					completionListeners = new ArrayList<>(listeners.size());
					Iterator<EventListener> iterator = listeners.iterator();
					while (iterator.hasNext()) {
						EventListener listener = iterator.next();
						if (listener instanceof CompletionListener) {
							completionListeners.add((CompletionListener) listener);
							iterator.remove();
						}
					}

					// notify all waiting threads
					stateSync.notifyAll();
				}
			}
		} finally {
			if (completed) {
				session.removeListener(this);

				// fire completed event
				invokeCompleted(completionListeners);
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

	public void addListener(EventListener listener) {
		Objects.requireNonNull(listener);

		if (listener instanceof CompletionListener) {
			CompletionListener completionListener = (CompletionListener) listener;
			boolean invokeNow = true;
			synchronized (stateSync) {
				if (state == FutureState.RUNNING) {
					listeners.add(listener);
					invokeNow = false;
				}
			}
			if (invokeNow) {
				// fire completion event for this listener
				invokeCompleted(Collections.singletonList(completionListener));
			}
		} else {
			synchronized (stateSync) {
				listeners.add(listener);
			}
		}
	}

	public void removeListener(EventListener listener) {
		Objects.requireNonNull(listener);

		if (listeners.contains(listener)) {
			synchronized (stateSync) {
				listeners.remove(listener);
			}
		}
	}

	private void invokeCompleted(List<CompletionListener> listeners) {
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

		CompletionEvent event = new CompletionEvent(this, value, error);
		for (CompletionListener listener : listeners)
			listener.completed(event);
	}

	@SuppressWarnings("serial")
	public static class CompletionEvent extends EventObject {
		private final Object value;
		private final Throwable error;

		private CompletionEvent(Object source, Object value, Throwable error) {
			super(source);

			this.value = value;
			this.error = error;
		}

		public Object getValue() {
			return this.value;
		}

		public Throwable getError() {
			return this.error;
		}
	}

	public interface CompletionListener extends EventListener {
		void completed(CompletionEvent event);
	}
}
