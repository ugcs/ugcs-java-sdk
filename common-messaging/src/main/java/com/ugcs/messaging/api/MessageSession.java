package com.ugcs.messaging.api;

import java.net.SocketAddress;

/**
 * Represents connection session within a peer-to-peer
 * communication and provides asynchronous party messages exchange.
 * Server-side parties construct sessions through acceptors and a new 
 * session is created on a new incoming connection. Client parties obtain 
 * sessions with help of connectors. Both acceptors and connectors are 
 * in charge of a proper session construction, that involves setup 
 * of message encoders and decoders.
 */
public interface MessageSession {
	SocketAddress getLocalAddress();
	SocketAddress getRemoteAddress();
	void addListener(MessageListener listener);
	void addListener(MessageListener listener, MessageSelector selector);
	void removeListener(MessageListener listener);
	void setAttribute(Object key, Object value);
	Object getAttribute(Object key);
	Object getAttribute(Object key, Object defaultValue);
	boolean isOpened();
	void close();
	void closeNonBlocking();
	void send(Object message);
}
