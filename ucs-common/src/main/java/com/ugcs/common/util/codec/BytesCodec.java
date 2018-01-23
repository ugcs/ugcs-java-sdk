package com.ugcs.common.util.codec;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.ugcs.common.util.Preconditions;

/**
 * Provides utility methods for encoding and decoding values of different type into and from array of bytes.
 */
public final class BytesCodec {

    private BytesCodec() {
    }

	/**
	 * Encodes custom object using the provided {@link ObjectCodec} for this object.
	 * @param obj - custom object to be encoded
	 * @param objectCodec - an instance of {@link ObjectCodec}, that implements logic to encode this object.
	 * @return <tt>null</tt>, if <tt>obj</tt> is <tt>null</tt>, encoded <tt>obj</tt> otherwise
	 */
	public static byte[] encodeObject(Object obj, ObjectCodec objectCodec) {
		if (obj == null) {
			return null;
		}

		Preconditions.checkNotNull(objectCodec);
		byte codecType = objectCodec.getType();

		if (codecType >= SystemCodecTypes.RESERVED_RANGE_FROM && codecType <= SystemCodecTypes.RESERVED_RANGE_TO) {
			throw new IllegalArgumentException(String.format(
					"The provided codec object must have the type out of system codec range [%d; %d]!"
                            + "type of provided codec = %d",
					SystemCodecTypes.RESERVED_RANGE_FROM,
					SystemCodecTypes.RESERVED_RANGE_TO,
					codecType));
		}

		byte[] encoded = objectCodec.encode(obj);
		return packEncoded(encoded, codecType);
	}

    /**
     * Encodes {@link Integer} value to variable length byte array, reducing the serialized size.
     * The first byte in encoded byte array has the value of {@link SystemCodecTypes#INTEGER_TYPE}.
     * @param value - {@link Integer} value to be serialized
     * @return <tt>null</tt>, if the <tt>value</tt> is <tt>null</tt>, encoded <tt>value</tt> otherwise
     */
    public static byte[] encodeInteger(Integer value) {
        if (value == null) {
            return null;
        }

        byte[] bytes = VarLength.encodeInt(value);
        return packEncoded(bytes, SystemCodecTypes.INTEGER_TYPE);
    }

    /**
     * Encodes {@link Long} value to variable length byte array, reducing the serialized size.
     * The first byte in encoded byte array has the value of  {@link SystemCodecTypes#LONG_TYPE}.
     * @param value - {@link Long} value to be serialized
     * @return <tt>null</tt>, if the <tt>value</tt> is <tt>null</tt>, encoded <tt>value</tt> otherwise
     */
    public static byte[] encodeLong(Long value) {
        if (value == null) {
            return null;
        }

        byte[] bytes = VarLength.encodeLong(value);
        return packEncoded(bytes, SystemCodecTypes.LONG_TYPE);
    }

    /**
     * Encodes {@link Double} value to variable length byte array, reducing the serialized size.
     * First, {@link Double} value is converted to long bits with <tt>doubleToLongBits</tt>, and then encoded.
     * The first byte in encoded byte array has the value of  {@link SystemCodecTypes#DOUBLE_TYPE}.
     * @param value - {@link Double} value to be serialized
     * @return <tt>null</tt>, if the <tt>value</tt> is <tt>null</tt>, encoded <tt>value</tt> otherwise
     */
    public static byte[] encodeDouble(Double value) {
        if (value == null) {
            return null;
        }

        long longBits = Double.doubleToLongBits(value);
        byte[] bytes = VarLength.encodeLong(longBits);
        return packEncoded(bytes, SystemCodecTypes.DOUBLE_TYPE);
    }

    /**
     * Encodes {@link Float} value to variable length byte array, reducing the serialized size.
     * First, {@link Float} value is converted to int bits with <tt>floatToIntBits</tt>, and then encoded.
     * The first byte in encoded byte array has the value of  {@link SystemCodecTypes#FLOAT_TYPE}.
     * @param value - {@link Float} value to be serialized
     * @return <tt>null</tt>, if the <tt>value</tt> is <tt>null</tt>, encoded <tt>value</tt> otherwise
     */
    public static byte[] encodeFloat(Float value) {
        if (value == null) {
            return null;
        }

        int intBits = Float.floatToIntBits(value);
        byte[] bytes = VarLength.encodeInt(intBits);
        return packEncoded(bytes, SystemCodecTypes.FLOAT_TYPE);
    }

    /**
     * Encodes {@link Boolean} value to variable length byte array, reducing the serialized size.
     * The first byte in encoded byte array has the value of  {@link SystemCodecTypes#BOOLEAN_TYPE}.
     * @param value - {@link Boolean} value to be serialized
     * @return <tt>null</tt>, if the <tt>value</tt> is <tt>null</tt>, encoded <tt>value</tt> otherwise
     */
    public static byte[] encodeBoolean(Boolean value) {
        if (value == null) {
            return null;
        }
        return new byte[] {SystemCodecTypes.BOOLEAN_TYPE, (value ? (byte)1 : (byte)0)};
    }

    /**
     * Encodes {@link String} value to variable length byte array, reducing the serialized size.
     * The first byte in encoded byte array has the value of  {@link SystemCodecTypes#STRING_TYPE}.
     * @param value - {@link String} value to be serialized
     * @return <tt>null</tt>, if the <tt>value</tt> is <tt>null</tt>, encoded <tt>value</tt> otherwise
     */
    public static byte[] encodeString(String value) {
        if (value == null) {
            return null;
        }

		byte[] encoded = value.getBytes(StandardCharsets.UTF_8);
		return packEncoded(encoded, SystemCodecTypes.STRING_TYPE);
    }

    /**
     * Decodes {@link Object} value from variable length byte array.
     * First, the byte array is decoded to the corresponding type, depending on value in the first byte of array.
     * Then, decoded value is returned as {@link Object} with no casting.
     * @param bytes encoded variable length byte array
     * @return decoded {@link Object} value
     */
    public static Object decodeObject(byte[] bytes, ObjectCodecContext codecContext) {
        if (bytes == null) {
            return null;
        }
        checkSize(bytes);
        byte type = bytes[0];
        byte[] data = Arrays.copyOfRange(bytes, 1, bytes.length);

        switch (type) {
            case SystemCodecTypes.INTEGER_TYPE:
                return VarLength.decodeInt(data);

            case SystemCodecTypes.LONG_TYPE:
                return VarLength.decodeLong(data);

            case SystemCodecTypes.DOUBLE_TYPE:
                long longBits = VarLength.decodeLong(data);
                return Double.longBitsToDouble(longBits);

            case SystemCodecTypes.FLOAT_TYPE:
                int intBits = VarLength.decodeInt(data);
                return Float.intBitsToFloat(intBits);

            case SystemCodecTypes.BOOLEAN_TYPE:
                return (data[0] == 1);

            case SystemCodecTypes.STRING_TYPE:
				return new String(data, StandardCharsets.UTF_8);

            default:
                Preconditions.checkNotNull(codecContext);
				ObjectCodec objectCodec = codecContext.byType(type);
				if (objectCodec == null) {
					throw new IllegalArgumentException(String.format("Value can not be decoded! "
                            + "No codec found for type {%d}", type));
				}
				return objectCodec.decode(data);
        }
    }

    /**
     * Decodes {@link Integer} value from variable length byte array.
     * First, the byte array is decoded to the corresponding type, depending on value in the first byte of array.
     * Then, decoded value is casted to Integer.
     * @param bytes encoded variable length byte array
     * @return decoded {@link Integer}  value
     */
    public static Integer decodeInteger(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        checkSize(bytes);
        byte type = bytes[0];
        byte[] data = Arrays.copyOfRange(bytes, 1, bytes.length);

        switch (type) {
            case SystemCodecTypes.INTEGER_TYPE:
                return VarLength.decodeInt(data);

            case SystemCodecTypes.LONG_TYPE:
                return (int)VarLength.decodeLong(data);

            case SystemCodecTypes.DOUBLE_TYPE:
                long longBits = VarLength.decodeLong(data);
                double d = Double.longBitsToDouble(longBits);
                return (int)d;

            case SystemCodecTypes.FLOAT_TYPE:
                int intBits = VarLength.decodeInt(data);
                float f = Float.intBitsToFloat(intBits);
                return (int)f;

            case SystemCodecTypes.BOOLEAN_TYPE:
                return (int)data[0];

            case SystemCodecTypes.STRING_TYPE:
                String str = new String(data, StandardCharsets.UTF_8);
				return Integer.valueOf(str);

            default:
                throw new IllegalArgumentException(
                        String.format("The encoded value is of unsupported type {%s}", type));
        }
    }

    /**
     * Decodes {@link Long} value from variable length byte array.
     * First, the byte array is decoded to the corresponding type, depending on value in the first byte of array.
     * Then, decoded value is casted to Long.
     * @param bytes encoded variable length byte array
     * @return decoded {@link Long} value
     */
    public static Long decodeLong(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        checkSize(bytes);

        byte type = bytes[0];
        byte[] data = Arrays.copyOfRange(bytes, 1, bytes.length);

        switch (type) {
            case SystemCodecTypes.LONG_TYPE:
                return VarLength.decodeLong(data);

            case SystemCodecTypes.INTEGER_TYPE:
                return (long)VarLength.decodeInt(data);

            case SystemCodecTypes.DOUBLE_TYPE:
                long longBits = VarLength.decodeLong(data);
                double d = Double.longBitsToDouble(longBits);
                return (long)d;

            case SystemCodecTypes.FLOAT_TYPE:
                int intBits = VarLength.decodeInt(data);
                float f = Float.intBitsToFloat(intBits);
                return (long)f;

            case SystemCodecTypes.BOOLEAN_TYPE:
                return (long)data[0];

            case SystemCodecTypes.STRING_TYPE:
                String str = new String(data, StandardCharsets.UTF_8);
                return Long.valueOf(str);

            default:
                throw new IllegalArgumentException(
                        String.format("The encoded value is of unsupported type {%s}", type));
        }
    }

    /**
     * Decodes {@link Double} value from variable length byte array.
     * First, the byte array is decoded to the corresponding type, depending on value in the first byte of array.
     * Then, decoded value is casted to Double.
     * @param bytes encoded variable length byte array
     * @return decoded {@link Double} value
     */
    public static Double decodeDouble(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        checkSize(bytes);

        byte type = bytes[0];
        byte[] data = Arrays.copyOfRange(bytes, 1, bytes.length);

        switch (type) {
            case SystemCodecTypes.DOUBLE_TYPE:
                long longBits = VarLength.decodeLong(data);
                return Double.longBitsToDouble(longBits);

            case SystemCodecTypes.LONG_TYPE:
                return (double)VarLength.decodeLong(data);

            case SystemCodecTypes.INTEGER_TYPE:
                return (double)VarLength.decodeInt(data);

            case SystemCodecTypes.FLOAT_TYPE:
                int intBits = VarLength.decodeInt(data);
                float f = Float.intBitsToFloat(intBits);
                return (double)f;

            case SystemCodecTypes.BOOLEAN_TYPE:
                return (double)data[0];

            case SystemCodecTypes.STRING_TYPE:
                String str = new String(data, StandardCharsets.UTF_8);
                return Double.valueOf(str);

            default:
                throw new IllegalArgumentException(
                        String.format("The encoded value is of unsupported type {%s}", type));
        }
    }

    /**
     * Decodes {@link Float} value from variable length byte array.
     * First, the byte array is decoded to the corresponding type, depending on value in the first byte of array.
     * Then, decoded value is casted to Float.
     * @param bytes encoded variable length byte array
     * @return decoded {@link Float} value
     */
    public static Float decodeFloat(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        checkSize(bytes);

        byte type = bytes[0];
        byte[] data = Arrays.copyOfRange(bytes, 1, bytes.length);

        switch (type) {
            case SystemCodecTypes.FLOAT_TYPE:
                int intBits = VarLength.decodeInt(data);
                return Float.intBitsToFloat(intBits);

            case SystemCodecTypes.LONG_TYPE:
                return (float)VarLength.decodeLong(data);

            case SystemCodecTypes.INTEGER_TYPE:
                return (float)VarLength.decodeInt(data);

            case SystemCodecTypes.DOUBLE_TYPE:
                long longBits = VarLength.decodeLong(data);
                double d = Double.longBitsToDouble(longBits);
                return (float)d;

            case SystemCodecTypes.BOOLEAN_TYPE:
                return (float)data[0];

            case SystemCodecTypes.STRING_TYPE:
                String str = new String(data, StandardCharsets.UTF_8);
                return Float.valueOf(str);

            default:
                throw new IllegalArgumentException(
                        String.format("The encoded value is of unsupported type {%s}", type));
        }
    }

    /**
     * Decodes {@link Boolean} value from variable length byte array.
     * First, the byte array is decoded to the corresponding type, depending on value in the first byte of array.
     * Then, decoded value is casted to Boolean.
     * @param bytes encoded variable length byte array
     * @return decoded {@link Boolean} value
     */
    public static Boolean decodeBoolean(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        checkSize(bytes);

        byte type = bytes[0];
        byte[] data = Arrays.copyOfRange(bytes, 1, bytes.length);

        switch (type) {
            case SystemCodecTypes.BOOLEAN_TYPE:
                return data[0] == (byte)1;

            case SystemCodecTypes.LONG_TYPE:
                return VarLength.decodeLong(data) != 0L;

            case SystemCodecTypes.INTEGER_TYPE:
                return VarLength.decodeInt(data) != 0;

            case SystemCodecTypes.DOUBLE_TYPE:
                long longBits = VarLength.decodeLong(data);
                double d = Double.longBitsToDouble(longBits);
                return d != 0.0d;

            case SystemCodecTypes.FLOAT_TYPE:
                int intBits = VarLength.decodeInt(data);
                float f = Float.intBitsToFloat(intBits);
                return f != 0.0f;

            case SystemCodecTypes.STRING_TYPE:
                String str = new String(data, StandardCharsets.UTF_8);
                return Boolean.valueOf(str);

            default:
                throw new IllegalArgumentException(
                        String.format("The encoded value is of unsupported type {%s}", type));
        }
    }

    /**
     * Decodes {@link String} value from variable length byte array.
     * First, the byte array is decoded to the corresponding type, depending on value in the first byte of array.
     * Then, decoded value is casted to String.
     * @param bytes encoded variable length byte array
     * @return decoded  {@link String} value
     */
    public static String decodeString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        checkSize(bytes);

        byte type = bytes[0];
        byte[] data = Arrays.copyOfRange(bytes, 1, bytes.length);

        switch (type) {
            case SystemCodecTypes.FLOAT_TYPE:
                int intBits = VarLength.decodeInt(data);
                float f = Float.intBitsToFloat(intBits);
                return String.valueOf(f);

            case SystemCodecTypes.LONG_TYPE:
                return String.valueOf(VarLength.decodeLong(data));

            case SystemCodecTypes.INTEGER_TYPE:
                return String.valueOf(VarLength.decodeInt(data));

            case SystemCodecTypes.DOUBLE_TYPE:
                long longBits = VarLength.decodeLong(data);
                double d = Double.longBitsToDouble(longBits);
                return String.valueOf(d);

            case SystemCodecTypes.BOOLEAN_TYPE:
                return String.valueOf(data[0]);

            case SystemCodecTypes.STRING_TYPE:
                return new String(data, StandardCharsets.UTF_8);

            default:
                throw new IllegalArgumentException(
                        String.format("The encoded value is of unsupported type {%s}", type));
        }
    }

    /**
     * Packs encoded bytes, setting the type to the first byte.
     */
    private static byte[] packEncoded(byte[] bytes, byte type) {
        byte[] result = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, result, 1, bytes.length);
        result[0] = type;
        return result;
    }

    private static void checkSize(byte[] bytes) {
        if (bytes.length < 1) {
            throw new IllegalArgumentException(
                    String.format("The length of input byte array must be > 0. The actual length is {%s}.",
                            bytes.length));
        }
    }
}
