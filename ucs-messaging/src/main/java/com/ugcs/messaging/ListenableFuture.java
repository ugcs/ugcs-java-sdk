package com.ugcs.messaging;

import java.util.concurrent.Future;

public interface ListenableFuture<T> extends Future<T> {

	void addCompletionListener(CompletionListener<T> listener);

	<R> ListenableFuture<R> map(Mapper<T, R> mapper);
}
