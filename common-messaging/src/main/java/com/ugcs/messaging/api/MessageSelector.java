package com.ugcs.messaging.api;

public interface MessageSelector {
	boolean select(Object message);
}
