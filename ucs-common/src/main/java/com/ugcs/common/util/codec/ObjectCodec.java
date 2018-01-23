package com.ugcs.common.util.codec;

/**
 * Provides methods to encode and decode custom objects.
 */
public interface ObjectCodec {

	/**
	 * Encodes custom object into array of bytes.
	 * @param value object to be encoded
	 * @return encoded object as array of bytes
	 */
	byte[] encode(Object value);

	/**
	 * Decodes custom object from array of bytes.
	 * @param bytes an array of encoded bytes
	 * @return an instance of custom object, decoded from bytes array.
	 */
	Object decode(byte[] bytes);

	/**
	 * Returns the type of {@link ObjectCodec}.
	 * <p>
	 * <b>Note, that byte values in range [{@link SystemCodecTypes#RESERVED_RANGE_FROM} -
	 * {@link SystemCodecTypes#RESERVED_RANGE_TO}] are reserved for system coding purposes
	 * (including encoding and decoding primitive types).
	 * This values can not be used in custom implementations</b>
	 *
	 * @return type of {@link ObjectCodec}
	 */
	byte getType();
}
