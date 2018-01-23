package com.ugcs.messaging.mina;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.mina.core.future.WriteFuture;

public class WriteFutureAdapter implements Future<Void> {

	private final WriteFuture writeFuture;

	public WriteFutureAdapter(WriteFuture writeFuture) {
		Objects.requireNonNull(writeFuture);

		this.writeFuture = writeFuture;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return writeFuture.isDone();
	}

	@Override
	public Void get() throws InterruptedException, ExecutionException {
		writeFuture.await();
		return getOrError();
	}

	@Override
	public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		writeFuture.await(timeout, unit);
		return getOrError();
	}

	private Void getOrError() throws ExecutionException {
		if (writeFuture.getException() != null)
			throw new ExecutionException(writeFuture.getException());
		if (!writeFuture.isWritten())
			throw new ExecutionException(new IOException("Write failed"));
		return null;
	}
}
