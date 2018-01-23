package com.ugcs.messaging.api;

@SuppressWarnings("serial")
public class MessageSessionErrorEvent extends MessageSessionEvent {

	private final Throwable cause;

	public MessageSessionErrorEvent(Object source, MessageSession session, Throwable cause) {
		super(source, session);

		this.cause = cause;
	}

	public Throwable getCause() {
		return cause;
	}
}
