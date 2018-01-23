package com.ugcs.messaging.api;

import java.util.EventListener;

public interface CloseListener extends EventListener {

	void closed(MessageSessionEvent event);

	void closeError(MessageSessionErrorEvent event);
}
