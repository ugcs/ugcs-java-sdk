package com.ugcs.messaging.api;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.Future;
import java.util.function.Supplier;

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

	<T> void setAttribute(Object key, T value);

	<T> T getAttribute(Object key);

	<T> T getAttribute(Object key, T defaultValue);

	<T> T getAttribute(Object key, Supplier<T> supplier);

	boolean isOpened();

	void close() throws IOException;

	void closeNonBlocking(CloseListener listener);

	Future<Void> send(Object message);
}
