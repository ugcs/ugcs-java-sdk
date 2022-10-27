package com.ugcs.messaging;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface ListenableFuture<T> extends Future<T> {

	void addCompletionListener(CompletionListener<T> listener);

	void awaitCompletionListeners(long timeout, TimeUnit unit)
			throws TimeoutException, InterruptedException;

	<R> ListenableFuture<R> map(Mapper<T, R> mapper);
}
