package com.ugcs.messaging.api;

import java.util.EventListener;

public interface MessageListener extends EventListener {

	void messageReceived(MessageEvent event);

	void cancelled();
}
