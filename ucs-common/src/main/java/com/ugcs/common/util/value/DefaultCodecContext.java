package com.ugcs.common.util.value;

import com.ugcs.common.util.codec.ObjectCodec;
import com.ugcs.common.util.codec.ObjectCodecContext;

public class DefaultCodecContext implements ObjectCodecContext {
	private static final DefaultCodecContext INSTANCE = new DefaultCodecContext();

	public static DefaultCodecContext getInstance() {
		return INSTANCE;
	}

	@Override
	public ObjectCodec byType(byte type) {
		if (type == MetaValueCodec.TYPE)
			return MetaValueCodec.getInstance();
		return null;
	}

	@Override
	public ObjectCodec byObjectType(Class<?> objectType) {
		if (MetaValue.class.equals(objectType))
			return MetaValueCodec.getInstance();
		return null;
	}
}