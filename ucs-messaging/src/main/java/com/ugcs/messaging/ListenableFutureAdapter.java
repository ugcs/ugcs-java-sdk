package com.ugcs.messaging;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ListenableFutureAdapter<T, R> extends AbstractListenableFuture<R> {

	private final ListenableFuture<T> future;
	private final Mapper<T, R> mapper;

	public ListenableFutureAdapter(ListenableFuture<T> future, Mapper<T, R> mapper) {
		Objects.requireNonNull(future);
		Objects.requireNonNull(mapper);

		this.future = future;
		this.mapper = mapper;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return future.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return future.isCancelled();
	}

	@Override
	public boolean isDone() {
		return future.isDone();
	}

	@Override
	public R get() throws InterruptedException, ExecutionException {
		T result = future.get();
		try {
			return mapper.map(result);
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
	}

	@Override
	public R get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		T result = future.get(timeout, unit);
		try {
			return mapper.map(result);
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
	}

	/* events */

	@Override
	public void addCompletionListener(final CompletionListener<R> listener) {
		Objects.requireNonNull(listener);

		CompletionListener<T> innerListener = new CompletionListener<T>() {
			@Override
			public void completed(CompletionEvent<T> event) {
				Object source = ListenableFutureAdapter.this;
				if (event.getError() != null) {
					// error translated from the lower level
					listener.completed(new CompletionEvent<R>(source, null, event.getError()));
				} else {
					R result = null;
					try {
						result = mapper.map(event.getResult());
					} catch (Exception e) {
						// error extracted from the response value
						listener.completed(new CompletionEvent<R>(source, null, e));
					}
					if (result != null) {
						// ok
						listener.completed(new CompletionEvent<R>(source, result, null));
					}
				}
			}
		};
		future.addCompletionListener(innerListener);
	}
}
