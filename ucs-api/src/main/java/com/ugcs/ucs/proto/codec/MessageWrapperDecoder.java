package com.ugcs.ucs.proto.codec;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ugcs.common.io.CircularBuffer;
import com.ugcs.messaging.api.CorruptedDataException;
import com.ugcs.messaging.api.MessageDecoder;

public class MessageWrapperDecoder implements MessageDecoder {

	private static final Logger log = LoggerFactory.getLogger(MessageWrapperDecoder.class);

	private static final int MAX_MESSAGE_LENGTH = 64 * 1024 * 1024; // 64 MB

	private final ProtoMessageDecoder protoDecoder;

	private final ProtoMessageMapping protoMapping;

	private final CircularBuffer decoderBuffer = new CircularBuffer();

	public MessageWrapperDecoder(ProtoMessageDecoder protoDecoder, ProtoMessageMapping protoMapping) {
		if (protoDecoder == null)
			throw new IllegalArgumentException("protoDecoder");
		if (protoMapping == null)
			throw new IllegalArgumentException("protoMapping");

		this.protoDecoder = protoDecoder;
		this.protoMapping = protoMapping;
	}

	private void fillBuffer(ByteBuffer buffer) throws IOException {
		if (buffer == null)
			return;
		OutputStream decoderOut = decoderBuffer.getOutputStream();
		WritableByteChannel channel = Channels.newChannel(decoderOut);
		channel.write(buffer);
	}

	private short readShort(InputStream in) throws IOException {
		int a = in.read();
		int b = in.read();
		if ((a | b) < 0)
			throw new EOFException();
		return (short)((a << 8) | b);
	}

	private int readInt(InputStream in) throws IOException {
		int a = in.read();
		int b = in.read();
		int c = in.read();
		int d = in.read();
		if ((a | b | c | d) < 0)
			throw new EOFException();
		return (a << 24) | (b << 16) | (c << 8) | d;
	}

	private boolean isDecodable(InputStream in) throws IOException {
		if (in == null)
			return false;
		if (!in.markSupported())
			throw new IllegalArgumentException("Mark support required");

		// 16 bytes is the smallest possible message length
		if (in.available() < 16)
			return false;
		// reading message length
		try {
			in.mark(16);
			in.skip(12);
			int messageLength = readInt(in);
			if (messageLength < 0 || messageLength > MAX_MESSAGE_LENGTH) {
				throw new CorruptedDataException("Garbage or corrupted data now receiving in connection");
			}
			if (in.available() < messageLength)
				return false;
		} finally {
			in.reset();
		}
		// ok
		return true;
	}

	private Object decodeFirst(InputStream in) throws Exception {
		// header
		int protocolSignature = readShort(in) & 0xffff;
		int protocolVersion = readShort(in) & 0xffff;
		int instanceId = readInt(in);
		int messageType = readInt(in);
		int messageLength = readInt(in);

		// message data
		byte[] messageData = new byte[messageLength];
		int messageBytesRead = in.read(messageData);
		if (messageBytesRead == -1) // empty message
			messageBytesRead = 0;
		if (messageBytesRead != messageLength) {
			// TODO possible alignment problem
			throw new IOException("Cannot read message data");
		}

		// checks: signature & version
		if (protocolSignature != Protocol.SIGNATURE)
			throw new CorruptedDataException("Protocol signature error");
		if (protocolVersion != Protocol.VERSION)
			throw new CorruptedDataException("Unsupported protocol version: " + protocolVersion);

		// well-formed message received
		Message protoMessage = protoDecoder.decode(messageData, protoMapping.getMessageClass(messageType));
		return new MessageWrapper(protoMessage, instanceId);
	}

	@Override
	public List<Object> decode(ByteBuffer buffer) throws IOException {
		fillBuffer(buffer);
		InputStream in = decoderBuffer.getInputStream();

		List<Object> result = new ArrayList<>();
		while (isDecodable(in)) {
			Object decodedMessage = null;
			try {
				decodedMessage = decodeFirst(in);
			} catch (Exception e) {
				if (log.isDebugEnabled())
					log.error("Decoder error", e);
				else
					log.warn("Decoder error: {}", e.getMessage());
			}
			if (decodedMessage != null) {
				if (log.isDebugEnabled())
					log.debug("<--- Decoded message:\n{}", decodedMessage);
				result.add(decodedMessage);
			}
		}
		return result;
	}

	@Override
	public void close() throws Exception {
	}
}
