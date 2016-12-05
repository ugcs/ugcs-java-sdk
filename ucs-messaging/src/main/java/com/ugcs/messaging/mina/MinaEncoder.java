package com.ugcs.messaging.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.ugcs.messaging.api.MessageEncoder;

public class MinaEncoder implements ProtocolEncoder {

	private final MessageEncoder encoder;
	
	public MinaEncoder(MessageEncoder encoder) {
		if (encoder == null)
			throw new IllegalArgumentException("encoder");
		
		this.encoder = encoder;
	}
	
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		byte[] encodedMessage = encoder.encode(message);
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
