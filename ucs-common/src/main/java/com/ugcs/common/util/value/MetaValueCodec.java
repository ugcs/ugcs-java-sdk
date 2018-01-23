package com.ugcs.common.util.value;

import com.ugcs.common.util.codec.ObjectCodec;

public class MetaValueCodec implements ObjectCodec {
	public static final byte TYPE = 42;
	private static final MetaValueCodec INSTANCE = new MetaValueCodec();

	public static MetaValueCodec getInstance() {
		return INSTANCE;
	}

	@Override
	public byte[] encode(Object value) {
		if (!(value instanceof MetaValue))
			return null;
		MetaValue metaValue = (MetaValue)value;
		return new byte[] {(byte)metaValue.ordinal()};
	}

	@Override
	public Object decode(byte[] bytes) {
		if (bytes == null)
			return null;
		if (bytes.length != 1)
			throw new IllegalArgumentException();
		return MetaValue.values()[bytes[0]];
	}

	@Override
	public byte getType() {
		return TYPE;
	}
}
