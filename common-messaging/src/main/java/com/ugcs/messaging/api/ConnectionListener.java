package com.ugcs.messaging.api;

import java.util.EventListener;

public interface ConnectionListener extends EventListener {
	void connected(MessageSessionEvent sessionEvent);
	void connectionError(MessageSessionErrorEvent sessionEvent);
}
