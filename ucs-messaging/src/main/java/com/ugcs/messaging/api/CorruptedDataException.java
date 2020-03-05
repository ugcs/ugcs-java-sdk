package com.ugcs.messaging.api;

import java.io.IOException;

public class CorruptedDataException extends IOException {
	public CorruptedDataException() {
		super();
	}

	public CorruptedDataException(String message) {
		super(message);
	}

	public CorruptedDataException(String message, Throwable cause) {
		super(message, cause);
	}
}
