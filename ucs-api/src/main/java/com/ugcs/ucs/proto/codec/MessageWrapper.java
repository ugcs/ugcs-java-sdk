package com.ugcs.ucs.proto.codec;

import com.google.protobuf.Message;

//--------------------------------
// 2: Protocol Signature
// 2: Protocol Version
// 4: Instance Identifier
// 4: Message Type
// 4: Message Length
// n: Message Value
//--------------------------------

public class MessageWrapper {
	private Message message;
	private int instanceId;
	
	public MessageWrapper() {
	}
	
	public MessageWrapper(Message message, int instanceId) {
		if (message == null)
			throw new IllegalArgumentException("message");
		
		this.message = message;
		this.instanceId = instanceId;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int messageInstanceId) {
		this.instanceId = messageInstanceId;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<MessageWrapper> {");
		sb.append("\n\tinstanceId: ");
		sb.append(Integer.toString(instanceId));
		sb.append("\n\tmessage: ");
		sb.append(message);
		sb.append("\n}");
		return sb.toString();
	}
}
