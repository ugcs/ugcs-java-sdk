package com.ugcs.messaging.api;

import java.util.EventObject;

@SuppressWarnings("serial")
public class MessageEvent extends EventObject {

	private final MessageSession session;
	private final Object message;

	public MessageEvent(Object source, MessageSession session, Object message) {
		super(source);

		this.session = session;
		this.message = message;
	}

	public MessageSession getSession() {
		return session;
	}

	public Object getMessage() {
		return message;
	}
}
