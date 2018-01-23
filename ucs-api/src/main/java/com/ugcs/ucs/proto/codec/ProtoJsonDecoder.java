package com.ugcs.ucs.proto.codec;

import java.lang.reflect.Method;
import java.nio.charset.Charset;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import com.googlecode.protobuf.format.JsonFormat.ParseException;

public class ProtoJsonDecoder implements ProtoMessageDecoder {

	private final Charset charset = Charset.forName("UTF-8");

	@Override
	public Message decode(byte[] buffer, Class<? extends Message> messageClass)
			throws InvalidProtocolBufferException, ParseException {
		return decode(buffer, createMessageBuilder(messageClass));
	}

	@Override
	public Message decode(byte[] buffer, Message.Builder builder)
			throws InvalidProtocolBufferException, ParseException {
		if (builder == null)
			throw new IllegalArgumentException("Message builder not specified");
		if (buffer == null)
			return null;

		String json = new String(buffer, charset);
		JsonFormat.merge(json, builder);
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
