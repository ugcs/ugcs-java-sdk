package com.ugcs.messaging.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageSessionListenerAdapter implements MessageSessionListener {

	private static final Logger log = LoggerFactory.getLogger(MessageSessionListenerAdapter.class);

	@Override
	public void sessionOpened(MessageSessionEvent event) {
		// adapter stub
	}

	@Override
	public void sessionClosed(MessageSessionEvent event) {
		// adapter stub
	}

	@Override
	public void sessionIdle(MessageSessionEvent event) {
		// adapter stub
	}

	@Override
	public void sessionError(MessageSessionEvent event) {
		// adapter stub
		if (event instanceof MessageSessionErrorEvent) {
			MessageSessionErrorEvent errorEvent = (MessageSessionErrorEvent)event;
			log.error("Session error", errorEvent.getCause());
		}
	}
}
