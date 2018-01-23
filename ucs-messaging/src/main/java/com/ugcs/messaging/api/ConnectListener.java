package com.ugcs.messaging.api;

import java.util.EventListener;

public interface ConnectListener extends EventListener {

	void connected(MessageSessionEvent event);

	void connectError(MessageSessionErrorEvent event);
}
