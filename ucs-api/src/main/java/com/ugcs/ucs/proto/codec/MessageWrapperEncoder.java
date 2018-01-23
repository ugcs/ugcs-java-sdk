package com.ugcs.ucs.proto.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;
import com.ugcs.messaging.api.MessageEncoder;

public class MessageWrapperEncoder implements MessageEncoder {

	private static final Logger log = LoggerFactory.getLogger(MessageWrapperEncoder.class);

	private final ProtoMessageEncoder protoEncoder;
	private final ProtoMessageMapping protoMapping;

	public MessageWrapperEncoder(ProtoMessageEncoder protoEncoder, ProtoMessageMapping protoMapping) {
		if (protoEncoder == null)
			throw new IllegalArgumentException("protoEncoder");
		if (protoMapping == null)
			throw new IllegalArgumentException("protoMapping");

		this.protoEncoder = protoEncoder;
		this.protoMapping = protoMapping;
	}

	private void writeShort(byte[] buffer, int position, short value) {
		buffer[position] = (byte)(value >> 8);
		buffer[position + 1] = (byte)value;
	}

	private void writeInt(byte[] buffer, int position, int value) {
		buffer[position] = (byte)(value >> 24);
		buffer[position + 1] = (byte)(value >> 16);
		buffer[position + 2] = (byte)(value >> 8);
		buffer[position + 3] = (byte)value;
	}

	@Override
	public byte[] encode(Object message) throws Exception {
		if (message == null)
			return new byte[0];
		if (!(message instanceof MessageWrapper))
			return new byte[0];

		if (log.isDebugEnabled())
			log.debug("---> Encoding message:\n{}", message);

		MessageWrapper messageWrapper = (MessageWrapper)message;
		Message protoMessage = messageWrapper.getMessage();
		int messageType = protoMapping.getMessageType(protoMessage.getClass());
		byte[] messageData = protoEncoder.encode(protoMessage);

		byte[] buffer = new byte[16 + messageData.length];
		writeShort(buffer, 0, (short)Protocol.SIGNATURE);
		writeShort(buffer, 2, (short)Protocol.VERSION);
		writeInt(buffer, 4, messageWrapper.getInstanceId());
		writeInt(buffer, 8, messageType);
		writeInt(buffer, 12, messageData.length);
		System.arraycopy(messageData, 0, buffer, 16, messageData.length);
		return buffer;
	}

	@Override
	public void close() throws Exception {
	}
}
