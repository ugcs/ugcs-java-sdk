package com.ugcs.messaging.api;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MessageFuture implements Future<Object>, MessageListener {
	private final MessageSession session;
	
	private FutureState state;
	private FutureResult result;
	
	private final Object stateSync = new Object();
	
	public MessageFuture(MessageSession session, Object message, MessageSelector selector) {
		if (session == null)
			throw new IllegalArgumentException("session");
		if (message == null)
			throw new IllegalArgumentException("message");
		
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
		try {
			return get(0L, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			throw new ExecutionException(e);
		}
	}

	public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		if (timeout < 0)
			throw new IllegalArgumentException("Timeout must be non-negative");
		if (unit == null)
			throw new IllegalArgumentException("Time unit not specified");
		
		synchronized (stateSync) {
			// wait if task is still running
			if (state == FutureState.RUNNING) {
				try {
					stateSync.wait(unit.toMillis(timeout));
				} catch (InterruptedException e) {
					// treat interrupted wait as cancel
					completeIfRunning(FutureState.CANCELLED, null, null);
				}
				// fail with timeout if task is running after awakening;
				// will also awake waiting threads on state change
				if (state == FutureState.RUNNING)
					completeIfRunning(FutureState.FAILED, null, new TimeoutException());
			}
			// obtain result
			return getResult();
		}
	}
	
	private void completeIfRunning(FutureState state, Object value, Throwable exception) {
		if (state == null || state == FutureState.RUNNING)
			throw new IllegalArgumentException("state");
		
		boolean removeListener = false;
		try {
			synchronized (stateSync) {
				if (this.state == FutureState.RUNNING) {
					this.state = state;
					this.result = new FutureResult(value, exception);
					removeListener = true;
					
					// notify all waiting threads
					stateSync.notifyAll();
				}
			}
		} finally {
			if (removeListener)
				session.removeListener(this);
		}
	}
	
	private Object getResult() throws InterruptedException, ExecutionException, TimeoutException {
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
	
	private void throwResultException() throws InterruptedException, ExecutionException, TimeoutException {
		try {
			throw result.getException();
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			throw e;
		} catch (Throwable e) {
			throw new ExecutionException(e);
		}
	}

	/* MessageListener implementation */
	
	@Override
	public void messageReceived(MessageEvent messageEvent) {
		if (messageEvent == null)
			throw new IllegalArgumentException("messageEvent");
		
		completeIfRunning(FutureState.SUCCEEDED, messageEvent.getMessage(), null);
	}
	
	@Override
	public void cancelled() {
		cancel(true);
	}
	
	/* inner classes */
	
	static enum FutureState {
		RUNNING,
		CANCELLED,
		FAILED,
		SUCCEEDED,
	}

	static class FutureResult {
		private final Object value;
		private final Throwable exception;
		
		FutureResult(Object value, Throwable exception) {
			this.value = value;
			this.exception = exception;
		}
		
		Object getValue() {
			return value;
		}
		
		Throwable getException() {
			return exception;
		}
	}
}
