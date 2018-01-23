package com.ugcs.messaging.api;

public interface HandlerFactory {

	MessageHandler getHandler(Class<?> messageType);
}
