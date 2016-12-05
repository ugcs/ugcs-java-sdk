package com.ugcs.common.util.value;

import com.ugcs.common.util.codec.BytesCodec;
import com.ugcs.common.util.codec.ObjectCodec;
import com.ugcs.common.util.codec.ObjectCodecContext;

public abstract class AbstractValue {
	private static final AbstractValue UNAVAILABLE = new Unavailable();

	private static final MetaCodec metaCodec = new MetaCodec();
	private static final MetaCodecContext metaCodecContext = new MetaCodecContext();

	public static AbstractValue of(Object value) {
		if (value == null)
			return null;
		if (value instanceof AbstractValue.Meta) {
			switch ((AbstractValue.Meta) value) {
				case UNAVAILABLE:
					return unavailable();
			}
		}
		if (value instanceof Boolean)
			return new BooleanValue((Boolean) value);
		if (value instanceof Integer)
			return new LongValue((Integer) value);
		if (value instanceof Long)
			return new LongValue((Long) value);
		if (value instanceof Float)
			return new FloatValue((Float) value);
		if (value instanceof Double)
			return new DoubleValue((Double) value);
		if (value instanceof String)
			return new StringValue((String) value);
		throw new IllegalArgumentException();
	}

	/* serialization */

	public abstract byte[] toBytes();

	public static AbstractValue fromBytes(byte[] bytes) {
		if (bytes == null)
			return null;
		return of(BytesCodec.decodeObject(bytes, metaCodecContext));
	}

	/* meta */

	public static AbstractValue unavailable() {
		return UNAVAILABLE;
	}

	public boolean isAvailable() {
		return true;
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
		return (int) longValue();
	}

	public abstract long longValue();

	public abstract float floatValue();

	public abstract double doubleValue();

	public abstract String stringValue();

	/* built-in classes */

	private static class Unavailable extends AbstractValue {

		public boolean isAvailable() {
			return false;
		}

		@Override
		public boolean booleanValue() {
			return false;
		}

		@Override
		public long longValue() {
			return 0L;
		}

		@Override
		public float floatValue() {
			return Float.NaN;
		}

		@Override
		public double doubleValue() {
			return Double.NaN;
		}

		@Override
		public String stringValue() {
			return null;
		}

		@Override
		public byte[] toBytes() {
			return BytesCodec.encodeObject(Meta.UNAVAILABLE, metaCodec);
		}
	}

	/* meta codec */

	private enum Meta {
		UNAVAILABLE
	}

	private static class MetaCodec implements ObjectCodec<AbstractValue.Meta> {
		public static final byte TYPE = 42;

		@Override
		public byte[] encode(AbstractValue.Meta value) {
			if (value == null)
				return null;
			return new byte[] {(byte) value.ordinal()};
		}

		@Override
		public AbstractValue.Meta decode(byte[] bytes) {
			if (bytes == null)
				return null;
			if (bytes.length != 1)
				throw new IllegalArgumentException();
			return AbstractValue.Meta.values()[bytes[0]];
		}

		@Override
		public byte getType() {
			return TYPE;
		}
	}

	private static class MetaCodecContext implements ObjectCodecContext {

		@Override
		public ObjectCodec byType(byte type) {
			if (type == MetaCodec.TYPE)
				return new MetaCodec();
			return null;
		}
	}
}
