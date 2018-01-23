package com.ugcs.common.util.codec;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import com.ugcs.common.util.Preconditions;

public final class VarLength {

	private VarLength() {
	}

	/**
	 * Encodes integer value to a variable length byte array.
	 * This approach reduces the size of serialized small positive integer values.
	 * @param i - integer value to be serialized
	 * @return a byte array representation of the integer value with the length from 1 to 5 bytes
	 */
	public static byte[] encodeInt(int i) {
		// byte 1
		int b1 = i & 0x7f;
		i >>>= 7;
		if (i == 0) {
			return new byte[] {(byte)b1};
		}
		b1 |= 0x80;

		// byte 2
		int b2 = i & 0x7f;
		i >>>= 7;
		if (i == 0) {
			return new byte[] {(byte)b1, (byte)b2};
		}
		b2 |= 0x80;

		// byte 3
		int b3 = i & 0x7f;
		i >>>= 7;
		if (i == 0) {
			return new byte[] {(byte)b1, (byte)b2, (byte)b3};
		}
		b3 |= 0x80;

		// byte 4
		int b4 = i & 0x7f;
		i >>>= 7;
		if (i == 0) {
			return new byte[] {(byte)b1, (byte)b2, (byte)b3, (byte)b4};
		}
		b4 |= 0x80;

		return new byte[] {(byte)b1, (byte)b2, (byte)b3, (byte)b4, (byte)i};
	}

	/**
	 * Encodes long value to a variable length byte array.
	 * This approach reduces the size of serialized small positive long values.
	 * @param l - long value to be serialized
	 * @return a byte array representation of the long value with the length from 1 to 10 bytes
	 */
	public static byte[] encodeLong(long l) {
		int i1 = ((int)l) & ((1 << 28) - 1); // bits 0..27
		int i2 = ((int)(l >>> 28)) & 0x7f; // bits 28..34
		int i3 = (int)(l >>> 35); // bits 35..63

		int b1 = i1 & 0x7f;
		i1 >>>= 7;
		if (i1 == 0 && i2 == 0 && i3 == 0) {
			return new byte[] {(byte)b1};
		}
		b1 |= 0x80;

		int b2 = i1 & 0x7f;
		i1 >>>= 7;
		if (i1 == 0 && i2 == 0 && i3 == 0) {
			return new byte[] {(byte)b1, (byte)b2};
		}
		b2 |= 0x80;

		int b3 = i1 & 0x7f;
		i1 >>>= 7;
		if (i1 == 0 && i2 == 0 && i3 == 0) {
			return new byte[] {(byte)b1, (byte)b2, (byte)b3};
		}
		b3 |= 0x80;

		int b4 = i1 & 0x7f;
		if (i2 == 0 && i3 == 0) {
			return new byte[] {(byte)b1, (byte)b2, (byte)b3, (byte)b4};
		}
		b4 |= 0x80;

		int b5 = i2;
		if (i3 == 0) {
			return new byte[] {(byte)b1, (byte)b2, (byte)b3, (byte)b4, (byte)b5};
		}
		b5 |= 0x80;

		byte[] i1i2Bytes = new byte[] {(byte)b1, (byte)b2, (byte)b3, (byte)b4, (byte)b5};

		byte[] i3Bytes = encodeInt(i3);
		byte[] result = new byte[5 + i3Bytes.length];
		System.arraycopy(i1i2Bytes, 0, result, 0, 5);
		System.arraycopy(i3Bytes, 0, result, 5, i3Bytes.length);
		return result;
	}

	/**
	 * Decodes integer value from variable length byte array.
	 * @param intBytes variable length byte array representing int value
	 * @return decoded int value
	 */
	public static int decodeInt(byte[] intBytes) {
		Preconditions.checkNotNull(intBytes);

		int pos = 0;
		int b;
		int result = intBytes[pos++] & 0xff;

		if ((result & 0x80) == 0) {
			return result;
		}
		result &= ~0x80;
		b = intBytes[pos++] & 0xff;
		result |= b << 7;

		if ((result & (0x80 << 7)) == 0) {
			return result;
		}
		result &= ~(0x80 << 7);
		b = intBytes[pos++] & 0xff;
		result |= b << 14;

		if ((result & (0x80 << 14)) == 0) {
			return result;
		}
		result &= ~(0x80 << 14);
		b = intBytes[pos++] & 0xff;
		result |= b << 21;

		if ((result & (0x80 << 21)) == 0) {
			return result;
		}
		result &= ~(0x80 << 21);
		b = intBytes[pos++] & 0xff;
		result |= b << 28;
		return result;
	}

	/**
	 * Decodes long value from variable length byte array.
	 * @param longBytes variable length byte array representing long value
	 * @return decoded long value
	 */
	public static long decodeLong(byte[] longBytes) {
		Preconditions.checkNotNull(longBytes);

		int pos = 0;
		int x;
		int res = longBytes[pos++] & 0xff;

		if ((res & 0x80) == 0) {
			return res;
		}
		res &= ~0x80;
		x = longBytes[pos++] & 0xff;
		res |= x << 7;

		if ((res & (0x80 << 7)) == 0) {
			return res;
		}
		res &= ~(0x80 << 7);
		x = longBytes[pos++] & 0xff;
		res |= x << 14;

		if ((res & (0x80 << 14)) == 0) {
			return res;
		}
		res &= ~(0x80 << 14);
		x = longBytes[pos++] & 0xff;
		res |= x << 21;

		if ((res & (0x80 << 21)) == 0) {
			return res;
		}
		res &= ~(0x80 << 21);
		x = longBytes[pos++] & 0xff;
		if ((x & 0x80) == 0) {
			return (((long)x) << 28) | res;
		}

		long resLong = (((long)(x & 0x7f)) << 28) | res;
		int remainLength = longBytes.length - pos;
		byte[] remainBytes = new byte[remainLength];
		System.arraycopy(longBytes, pos, remainBytes, 0, remainLength);
		return (((long)decodeInt(remainBytes)) << 35) | resLong;
	}

	/**
	 * Reads variable length decoded integer value from provided {@link InputStream}.
	 * @param is - {@link InputStream} to read value from
	 * @return read int value
	 */
	public static int readVarInt(InputStream is) throws IOException {
		Preconditions.checkNotNull(is);

		int result = is.read();
		int x;
		if (result == -1) {
			throw new EOFException();
		}
		if ((result & 0x80) == 0) {
			return result;
		}

		result &= ~0x80;
		if ((x = is.read()) == -1) {
			throw new EOFException();
		}
		result |= x << 7;
		if ((result & (0x80 << 7)) == 0) {
			return result;
		}

		result &= ~(0x80 << 7);
		if ((x = is.read()) == -1) {
			throw new EOFException();
		}
		result |= x << 14;
		if ((result & (0x80 << 14)) == 0) {
			return result;
		}

		result &= ~(0x80 << 14);
		if ((x = is.read()) == -1) {
			throw new EOFException();
		}
		result |= x << 21;
		if ((result & (0x80 << 21)) == 0) {
			return result;
		}

		result &= ~(0x80 << 21);
		if ((x = is.read()) == -1) {
			throw new EOFException();
		}
		result |= x << 28;

		return result;
	}

	/**
	 * Reads variable length long value from the provided {@link InputStream}.
	 * @param is - {@link InputStream} to read value from
	 * @return read long value
	 */
	public static long readVarLong(InputStream is) throws IOException {
		Preconditions.checkNotNull(is);

		int result = is.read();
		int x;
		if (result == -1) {
			throw new EOFException();
		}
		if ((result & 0x80) == 0) {
			return result;
		}

		result &= ~0x80;
		if ((x = is.read()) == -1) {
			throw new EOFException();
		}
		result |= x << 7;
		if ((result & (0x80 << 7)) == 0) {
			return result;
		}

		result &= ~(0x80 << 7);
		if ((x = is.read()) == -1) {
			throw new EOFException();
		}
		result |= x << 14;
		if ((result & (0x80 << 14)) == 0) {
			return result;
		}

		result &= ~(0x80 << 14);
		if ((x = is.read()) == -1) {
			throw new EOFException();
		}
		result |= x << 21;
		if ((result & (0x80 << 21)) == 0) {
			return result;
		}

		result &= ~(0x80 << 21);
		if ((x = is.read()) == -1) {
			throw new EOFException();
		}
		if ((x & 0x80) == 0) {
			return (((long)x) << 28) | result;
		}

		long resLong = (((long)(x & 0x7f)) << 28) | result;
		return (((long)readVarInt(is)) << 35) | resLong;
	}
}
