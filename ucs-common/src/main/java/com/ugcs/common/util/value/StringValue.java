package com.ugcs.common.util.value;

import java.util.Objects;

import com.ugcs.common.util.codec.BytesCodec;

public class StringValue extends AbstractValue {
	private final String value;

	public StringValue(String value) {
		Objects.requireNonNull(value);

		this.value = value;
	}

	@Override
	public byte[] toBytes() {
		return BytesCodec.encodeString(value);
	}

	/* converters */

	@Override
	public boolean booleanValue() {
		return Boolean.parseBoolean(value);
	}

	@Override
	public long longValue() {
		return Long.parseLong(value);
	}

	@Override
	public float floatValue() {
		return Float.parseFloat(value);
	}

	@Override
	public double doubleValue() {
		return Double.parseDouble(value);
	}

	@Override
	public String stringValue() {
		return value;
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
		return Objects.equals(this.value, ((AbstractValue)other).stringValue());
	}
}
