package com.ugcs.ucs.proto.codec;

import java.lang.reflect.Method;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

public class ProtoBinaryDecoder implements ProtoMessageDecoder {

	@Override
	public Message decode(byte[] buffer, Class<? extends Message> messageClass) throws InvalidProtocolBufferException {
		return decode(buffer, createMessageBuilder(messageClass));
	}

	@Override
	public Message decode(byte[] buffer, Message.Builder builder) throws InvalidProtocolBufferException {
		if (builder == null)
			throw new IllegalArgumentException("Message builder not specified");
		if (buffer == null)
			return null;

		builder.mergeFrom(buffer);
		return builder.build();
	}

	private Message.Builder createMessageBuilder(Class<? extends Message> messageClass) {
		if (messageClass == null)
			throw new IllegalArgumentException("Message class not specified");

		Message.Builder builder = null;
		try {
			Method method = messageClass.getMethod("newBuilder");
			builder = (Message.Builder)method.invoke(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return builder;
	}
}
