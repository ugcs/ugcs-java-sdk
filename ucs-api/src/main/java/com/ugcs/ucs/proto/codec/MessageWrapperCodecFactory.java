package com.ugcs.ucs.proto.codec;

import com.ugcs.messaging.api.CodecFactory;
import com.ugcs.messaging.api.MessageDecoder;
import com.ugcs.messaging.api.MessageEncoder;

public class MessageWrapperCodecFactory implements CodecFactory {

	private final ProtoMessageMapping protoMapping;

	public MessageWrapperCodecFactory(ProtoMessageMapping protoMapping) {
		if (protoMapping == null)
			throw new IllegalArgumentException("protoMapping");

		this.protoMapping = protoMapping;
	}

	@Override
	public MessageEncoder getEncoder() {
		return new MessageWrapperEncoder(
				new ProtoBinaryEncoder(), protoMapping);
	}

	@Override
	public MessageDecoder getDecoder() {
		return new MessageWrapperDecoder(
				new ProtoBinaryDecoder(), protoMapping);
	}
}
