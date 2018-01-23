package com.ugcs.common.util.value;

import java.util.Arrays;
import java.util.Objects;

import com.ugcs.common.util.codec.BytesCodec;
import com.ugcs.common.util.codec.ObjectCodecContext;

public abstract class AbstractValue {
	private static final AbstractValue UNAVAILABLE = new ObjectValue(
			MetaValue.UNAVAILABLE, MetaValueCodec.getInstance());
	private static final byte[] ENCODED_UNAVAILABLE = UNAVAILABLE.toBytes();

	public static AbstractValue of(Object value) {
		return of(value, DefaultCodecContext.getInstance());
	}

	public static AbstractValue of(Object value, ObjectCodecContext codecContext) {
		Objects.requireNonNull(codecContext);
		if (value == null)
			return null;
		if (value instanceof Boolean)
			return new BooleanValue((Boolean)value);
		if (value instanceof Integer)
			return new LongValue((Integer)value);
		if (value instanceof Long)
			return new LongValue((Long)value);
		if (value instanceof Float)
			return new FloatValue((Float)value);
		if (value instanceof Double)
			return new DoubleValue((Double)value);
		if (value instanceof String)
			return new StringValue((String)value);
		// default
		return new ObjectValue(value, codecContext.byObjectType(value.getClass()));
	}

	/* serialization */

	public abstract byte[] toBytes();

	public static AbstractValue fromBytes(byte[] bytes) {
		return fromBytes(bytes, DefaultCodecContext.getInstance());
	}

	public static AbstractValue fromBytes(byte[] bytes, ObjectCodecContext codecContext) {
		Objects.requireNonNull(codecContext);
		if (bytes == null)
			return null;
		return of(BytesCodec.decodeObject(bytes, codecContext), codecContext);
	}

	/* meta */

	public static AbstractValue unavailable() {
		return UNAVAILABLE;
	}

	public boolean isAvailable() {
		return true;
	}

	public static boolean isAvailable(byte[] bytes) {
		return !Arrays.equals(bytes, ENCODED_UNAVAILABLE);
	}

	/* defaults */

	public boolean booleanOrDefault(boolean defaultValue) {
		return isAvailable() ? booleanValue() : defaultValue;
	}

	public int intOrDefault(int defaultValue) {
		return isAvailable() ? intValue() : defaultValue;
	}

	public long longOrDefault(long defaultValue) {
		return isAvailable() ? longValue() : defaultValue;
	}

	public float floatOrDefault(float defaultValue) {
		return isAvailable() ? floatValue() : defaultValue;
	}

	public float floatOrNaN() {
		return floatOrDefault(Float.NaN);
	}

	public double doubleOrDefault(double defaultValue) {
		return isAvailable() ? doubleValue() : defaultValue;
	}

	public double doubleOrNaN() {
		return doubleOrDefault(Double.NaN);
	}

	public String stringOrDefault(String defaultValue) {
		return isAvailable() ? stringValue() : defaultValue;
	}

	/* converters */

	public abstract boolean booleanValue();

	public int intValue() {
		return (int)longValue();
	}

	public abstract long longValue();

	public abstract float floatValue();

	public abstract double doubleValue();

	public abstract String stringValue();

	public abstract Object objectValue();

	@Override
	public String toString() {
		return stringValue();
	}
}
