package com.ugcs.common.util.codec;

public interface ObjectCodecContext {

	ObjectCodec byType(byte type);

	ObjectCodec byObjectType(Class<?> objectType);
}
