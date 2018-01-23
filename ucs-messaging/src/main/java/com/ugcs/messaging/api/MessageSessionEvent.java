package com.ugcs.messaging.api;

import java.util.EventObject;

@SuppressWarnings("serial")
public class MessageSessionEvent extends EventObject {

	private final MessageSession session;

	public MessageSessionEvent(Object source, MessageSession session) {
		super(source);

		this.session = session;
	}

	public MessageSession getSession() {
		return session;
	}
}
