package com.ugcs.common.util.value;

import java.util.Objects;

import com.ugcs.common.util.Preconditions;
import com.ugcs.common.util.codec.BytesCodec;
import com.ugcs.common.util.codec.ObjectCodec;

public class ObjectValue extends AbstractValue {
	private final Object value;
	private final ObjectCodec codec;

	public ObjectValue(Object value, ObjectCodec codec) {
		Preconditions.checkNotNull(codec);

		this.value = value;
		this.codec = codec;
	}

	@Override
	public byte[] toBytes() {
		return BytesCodec.encodeObject(value, codec);
	}

	@Override
	public boolean isAvailable() {
		return value != MetaValue.UNAVAILABLE;
	}

	@Override
	public boolean booleanValue() {
		return false;
	}

	@Override
	public long longValue() {
		return 0;
	}

	@Override
	public float floatValue() {
		return 0;
	}

	@Override
	public double doubleValue() {
		return 0;
	}

	@Override
	public String stringValue() {
		return value.toString();
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
		return Objects.equals(this.value, ((AbstractValue)other).objectValue());
	}
}
