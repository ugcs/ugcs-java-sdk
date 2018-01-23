package com.ugcs.messaging.api;

import java.io.IOException;
import java.net.SocketAddress;

public interface Connector {

	void addSessionListener(MessageSessionListener sessionListener);

	void removeSessionListener(MessageSessionListener sessionListener);

	MessageSession connect(SocketAddress address) throws IOException;

	void connectNonBlocking(SocketAddress address, ConnectListener listener);

	void close() throws IOException;
}
