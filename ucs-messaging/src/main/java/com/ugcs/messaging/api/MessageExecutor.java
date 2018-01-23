package com.ugcs.messaging.api;

public class MessageExecutor {

	private final MessageSession session;

	public MessageExecutor(MessageSession session) {
		if (session == null)
			throw new IllegalArgumentException("session");

		this.session = session;
	}

	public MessageSession getSession() {
		return session;
	}

	public MessageFuture submit(Object message, MessageSelector selector) {
		return new MessageFuture(session, message, selector);
	}
}
