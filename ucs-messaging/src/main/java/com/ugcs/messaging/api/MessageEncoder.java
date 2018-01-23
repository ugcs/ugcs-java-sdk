package com.ugcs.messaging.api;

/**
 * Message encoder serializes messages into array of bytes.
 */
public interface MessageEncoder {

	byte[] encode(Object message) throws Exception;

	void close() throws Exception;
}
