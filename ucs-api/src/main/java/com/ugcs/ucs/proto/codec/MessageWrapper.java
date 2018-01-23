package com.ugcs.ucs.proto.codec;

import java.util.Objects;

import com.google.protobuf.Message;

public class MessageWrapper {

	private final Message message;
	private final int instanceId;

	public MessageWrapper(Message message, int instanceId) {
		Objects.requireNonNull(message);

		this.message = message;
		this.instanceId = instanceId;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public Message getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("<MessageWrapper> {")
				.append("\n\tinstanceId: ")
				.append(Integer.toString(instanceId))
				.append("\n\tmessage: ")
				.append(message)
				.append("\n}")
				.toString();
	}
}
