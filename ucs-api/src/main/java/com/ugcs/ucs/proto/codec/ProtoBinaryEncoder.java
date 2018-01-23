package com.ugcs.ucs.proto.codec;

import com.google.protobuf.Message;

public class ProtoBinaryEncoder implements ProtoMessageEncoder {

	@Override
	public byte[] encode(Message message) {
		if (message == null)
			return new byte[0];

		return message.toByteArray();
	}
}
