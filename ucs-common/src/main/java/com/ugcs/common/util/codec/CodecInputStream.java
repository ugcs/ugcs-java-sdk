package com.ugcs.common.util.codec;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This implementation extends {@link DataInputStream} and provides new methods to read variable-length parameters
 * of different types from the provided {@link InputStream}.
 */
public class CodecInputStream extends DataInputStream {

	public CodecInputStream(InputStream in) {
		super(in);
	}

	public int readVarInt() throws IOException {
		return VarLength.readVarInt(in);
	}

	public Integer readNullableVarInt() throws IOException {
		int isNull = read();
		if (isNull == 1) {
			return null;
		}

		return readVarInt();
	}

	public long readVarLong() throws IOException {
		return VarLength.readVarLong(in);
	}

	public Long readNullableVarLong() throws IOException {
		int isNull = read();
		if (isNull == 1) {
			return null;
		}

		return readVarLong();
	}

	public double readVarDouble() throws IOException {
		long longBits = readVarLong();
		return Double.longBitsToDouble(longBits);
	}

	public Double readNullableVarDouble() throws IOException {
		int isNull = read();
		if (isNull == 1) {
			return null;
		}

		return readVarDouble();
	}

	public float readVarFloat() throws IOException {
		int intBits = readVarInt();
		return Float.intBitsToFloat(intBits);
	}

	public Float readNullableVarFloat() throws IOException {
		int isNull = read();
		if (isNull == 1) {
			return null;
		}

		return readVarFloat();
	}

	public String readVarString() throws IOException {
		int length = readVarInt();
		char[] chars = new char[length];
		for (int i = 0; i < length; i++)
			chars[i] = readChar();
		return new String(chars);
	}

	public String readNullableVarString() throws IOException {
		int isNull = read();
		if (isNull == 1) {
			return null;
		}

		return readVarString();
	}

	public byte[] readVarBytes() throws IOException {
		int length = readVarInt();
		byte[] result = new byte[length];
		readFully(result);
		return result;
	}

	public byte[] readNullableVarBytes() throws IOException {
		int isNull = read();
		if (isNull == 1) {
			return null;
		}

		return readVarBytes();
	}

	public Boolean readNullableBoolean() throws IOException {
		int isNull = read();
		if (isNull == 1) {
			return null;
		}

		return readBoolean();
	}
}
