package com.ugcs.messaging.api;

@FunctionalInterface
public interface MessageSelector {

	boolean select(Object message);
}
