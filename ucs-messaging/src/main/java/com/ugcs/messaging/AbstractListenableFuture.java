package com.ugcs.messaging;

import java.util.Objects;

public abstract class AbstractListenableFuture<T> implements ListenableFuture<T> {

	@Override
	public <R> ListenableFuture<R> map(Mapper<T, R> mapper) {
		Objects.requireNonNull(mapper);

		return new ListenableFutureAdapter<>(this, mapper);
	}
}
