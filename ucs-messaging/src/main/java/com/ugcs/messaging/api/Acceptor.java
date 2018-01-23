package com.ugcs.messaging.api;

import java.io.IOException;
import java.net.SocketAddress;

public interface Acceptor {

	void addSessionListener(MessageSessionListener sessionListener);

	void removeSessionListener(MessageSessionListener sessionListener);

	void start(SocketAddress socketAddress) throws IOException;

	void stop() throws IOException;

	void close() throws IOException;
}
