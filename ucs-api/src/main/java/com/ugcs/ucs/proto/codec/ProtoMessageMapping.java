package com.ugcs.ucs.proto.codec;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.Message;

public abstract class ProtoMessageMapping {

	private Map<Integer, Class<? extends Message>> typeToClassMap =
			new HashMap<Integer, Class<? extends Message>>();
	private Map<Class<? extends Message>, Integer> classToTypeMap =
			new HashMap<Class<? extends Message>, Integer>();

	protected void putMapping(Integer messageType, Class<? extends Message> messageClass) {
		typeToClassMap.put(messageType, messageClass);
		classToTypeMap.put(messageClass, messageType);
	}

	public Class<? extends Message> getMessageClass(Integer messageType) {
		return typeToClassMap.get(messageType);
	}

	public Integer getMessageType(Class<? extends Message> messageClass) {
		return classToTypeMap.get(messageClass);
	}

	public Integer getMessageType(Message message) {
		if (message == null)
			throw new IllegalArgumentException("message");

		return classToTypeMap.get(message.getClass());
	}
}
