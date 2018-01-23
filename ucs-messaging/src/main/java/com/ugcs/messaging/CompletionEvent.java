package com.ugcs.messaging;

import java.util.EventObject;

public class CompletionEvent<T> extends EventObject {

	private final T result;
	private final Throwable error;

	public CompletionEvent(Object source, T result, Throwable error) {
		super(source);

		this.result = result;
		this.error = error;
	}

	public T getResult() {
		return this.result;
	}

	public Throwable getError() {
		return this.error;
	}
}