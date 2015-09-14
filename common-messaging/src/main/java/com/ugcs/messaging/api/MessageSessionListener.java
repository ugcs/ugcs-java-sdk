package com.ugcs.messaging.api;

import java.util.EventListener;

public interface MessageSessionListener extends EventListener {
	void sessionOpened(MessageSessionEvent sessionEvent);
	void sessionClosed(MessageSessionEvent sessionEvent);
	void sessionIdle(MessageSessionEvent sessionEvent);
	void sessionError(MessageSessionEvent sessionEvent);
}
