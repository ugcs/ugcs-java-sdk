package com.ugcs.ucs.proto.codec;

import com.google.protobuf.Message;

public interface ProtoMessageEncoder {

	byte[] encode(Message message);
}
