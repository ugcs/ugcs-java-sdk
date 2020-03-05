package com.ugcs.messaging.mina;

import java.io.IOException;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ugcs.messaging.api.MessageDecoder;

public class MinaDecoder implements ProtocolDecoder {

	private static final Logger log = LoggerFactory.getLogger(MinaDecoder.class);

	private final MessageDecoder decoder;

	public MinaDecoder(MessageDecoder decoder) {
		if (decoder == null)
			throw new IllegalArgumentException("decoder");

		this.decoder = decoder;
	}

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		List<Object> decodedObjects;
		try {
			decodedObjects = decoder.decode(in.buf());
		} catch (IOException e) {
			log.error("Corrupted data received", e);
			session.closeNow();
			throw e;
		} catch (RuntimeException e) {
			log.error("Message decoder error", e);
			throw e;
		}
		for (Object decodedObject : decodedObjects)
			out.write(decodedObject);
	}

	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
	}

	@Override
	public void dispose(IoSession session) throws Exception {
		decoder.close();
	}
}

