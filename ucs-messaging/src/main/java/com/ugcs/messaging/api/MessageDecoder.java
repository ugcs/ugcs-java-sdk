package com.ugcs.messaging.api;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Message decoder extracts message objects from a byte array.
 */
public interface MessageDecoder {

	List<Object> decode(ByteBuffer buffer) throws IOException;

	void close() throws Exception;
}
