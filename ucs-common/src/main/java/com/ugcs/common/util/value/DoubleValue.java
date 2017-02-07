package com.ugcs.common.util.value;

import com.ugcs.common.util.codec.BytesCodec;

public class DoubleValue extends AbstractValue {
	private final double value;

	public DoubleValue(double value) {
		this.value = value;
	}

	@Override
	public byte[] toBytes() {
		return BytesCodec.encodeDouble(value);
	}

	/* converters */

	@Override
	public boolean booleanValue() {
		return value != 0.;
	}

	@Override
	public long longValue() {
		return (long) value;
	}

	@Override
	public float floatValue() {
		return (float) value;
	}

	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public String stringValue() {
		return Double.toString(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AbstractValue))
			return false;
		return this.value == ((AbstractValue) obj).doubleValue();
	}
}
