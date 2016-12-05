package com.ugcs.common.util.value;

import com.ugcs.common.util.codec.BytesCodec;

public class LongValue extends AbstractValue {
	private final long value;

	public LongValue(long value) {
		this.value = value;
	}

	@Override
	public byte[] toBytes() {
		return BytesCodec.encodeLong(value);
	}

	/* converters */

	@Override
	public boolean booleanValue() {
		return value != 0;
	}

	@Override
	public long longValue() {
		return value;
	}

	@Override
	public float floatValue() {
		return (float) value;
	}

	@Override
	public double doubleValue() {
		return (double) value;
	}

	@Override
	public String stringValue() {
		return Long.toString(value);
	}
}
