package com.ugcs.common.util.value;

import com.ugcs.common.util.codec.BytesCodec;

public class FloatValue extends AbstractValue {
	private final float value;

	public FloatValue(float value) {
		this.value = value;
	}

	@Override
	public byte[] toBytes() {
		return BytesCodec.encodeFloat(value);
	}

	/* converters */

	@Override
	public boolean booleanValue() {
		return value != 0f;
	}

	@Override
	public long longValue() {
		return (long) value;
	}

	@Override
	public float floatValue() {
		return value;
	}

	@Override
	public double doubleValue() {
		return (double) value;
	}

	@Override
	public String stringValue() {
		return Float.toString(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AbstractValue))
			return false;
		return this.value == ((AbstractValue) obj).floatValue();
	}
}
