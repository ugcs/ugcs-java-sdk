package com.ugcs.common.util.value;

import com.ugcs.common.util.codec.BytesCodec;

public class BooleanValue extends AbstractValue {
	private final boolean value;

	public BooleanValue(boolean value) {
		this.value = value;
	}

	@Override
	public byte[] toBytes() {
		return BytesCodec.encodeBoolean(value);
	}

	/* converters */

	@Override
	public boolean booleanValue() {
		return value;
	}

	@Override
	public long longValue() {
		return value ? 1L : 0L;
	}

	@Override
	public float floatValue() {
		return value ? 1f : 0f;
	}

	@Override
	public double doubleValue() {
		return value ? 1. : 0.;
	}

	@Override
	public String stringValue() {
		return Boolean.toString(value);
	}

	@Override
	public Object objectValue() {
		return value;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof AbstractValue))
			return false;
		return this.value == ((AbstractValue)other).booleanValue();
	}
}
