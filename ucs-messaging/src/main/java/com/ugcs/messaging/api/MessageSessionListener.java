package com.ugcs.messaging.api;

import java.util.EventListener;

public interface MessageSessionListener extends EventListener {

	void sessionOpened(MessageSessionEvent event);

	void sessionClosed(MessageSessionEvent event);

	void sessionIdle(MessageSessionEvent event);

	void sessionError(MessageSessionEvent event);
}
