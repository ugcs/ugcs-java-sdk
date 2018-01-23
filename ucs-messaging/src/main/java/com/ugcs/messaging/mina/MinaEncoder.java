package com.ugcs.messaging.mina;

import com.ugcs.messaging.api.MessageEncoder;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaEncoder implements ProtocolEncoder {

	private static final Logger log = LoggerFactory.getLogger(MinaEncoder.class);
	private final MessageEncoder encoder;

	public MinaEncoder(MessageEncoder encoder) {
		if (encoder == null)
			throw new IllegalArgumentException("encoder");

		this.encoder = encoder;
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		byte[] encodedMessage;
		try {
			encodedMessage = encoder.encode(message);
		} catch (Throwable e) {
			log.error("Message encoder error", e);
			throw e;
		}
		if (encodedMessage != null && encodedMessage.length > 0) {
			IoBuffer buffer = IoBuffer.wrap(encodedMessage);
			out.write(buffer);
			out.flush();
		}
	}

	@Override
	public void dispose(IoSession session) throws Exception {
		encoder.close();
	}
}
