package com.ugcs.ucs.proto.codec;

import java.nio.charset.Charset;

import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;

public class ProtoJsonEncoder implements ProtoMessageEncoder {

	private final Charset charset = Charset.forName("UTF-8");

	@Override
	public byte[] encode(Message message) {
		if (message == null)
			return new byte[0];

		String json = JsonFormat.printToString(message);
		return json.getBytes(charset);
	}
}
