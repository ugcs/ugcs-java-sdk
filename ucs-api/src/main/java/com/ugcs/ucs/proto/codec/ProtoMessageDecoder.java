package com.ugcs.ucs.proto.codec;

import com.google.protobuf.Message;

public interface ProtoMessageDecoder {

	Message decode(byte[] buffer, Class<? extends Message> messageClass) throws Exception;

	Message decode(byte[] buffer, Message.Builder builder) throws Exception;
}
