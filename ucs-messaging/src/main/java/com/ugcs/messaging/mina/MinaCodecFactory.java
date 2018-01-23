package com.ugcs.messaging.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.ugcs.messaging.api.CodecFactory;

public class MinaCodecFactory implements ProtocolCodecFactory {

	private final CodecFactory codecFactory;

	private static final String ENCODER_ATTRIBUTE = "minaEncoder";
	private static final String DECODER_ATTRIBUTE = "minaDecoder";

	public MinaCodecFactory(CodecFactory codecFactory) {
		if (codecFactory == null)
			throw new IllegalArgumentException("codecFactory");

		this.codecFactory = codecFactory;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		MinaEncoder minaEncoder = null;
		Object value = session.getAttribute(ENCODER_ATTRIBUTE);
		if (value != null && value instanceof MinaEncoder)
			minaEncoder = (MinaEncoder)value;
		if (minaEncoder == null) {
			minaEncoder = new MinaEncoder(codecFactory.getEncoder());
			session.setAttribute(ENCODER_ATTRIBUTE, minaEncoder);
		}
		return minaEncoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		MinaDecoder minaDecoder = null;
		Object value = session.getAttribute(DECODER_ATTRIBUTE);
		if (value != null && value instanceof MinaDecoder)
			minaDecoder = (MinaDecoder)value;
		if (minaDecoder == null) {
			minaDecoder = new MinaDecoder(codecFactory.getDecoder());
			session.setAttribute(DECODER_ATTRIBUTE, minaDecoder);
		}
		return minaDecoder;
	}
}
