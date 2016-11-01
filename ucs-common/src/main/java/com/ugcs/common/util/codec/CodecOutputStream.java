package com.ugcs.common.util.codec;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.ugcs.common.util.Preconditions;

/**
 * This implementation extends {@link java.io.DataOutputStream} and provides new methods to write variable-length encoded
 * parameters of different types to the provided {@link OutputStream}.
 */
public class CodecOutputStream extends DataOutputStream {

	public CodecOutputStream(OutputStream out) {
		super(out);
	}

	public void writeVarInt(int i) throws IOException {
		byte[] encoded = VarLength.encodeInt(i);
		write(encoded);
	}

	public void writeNullableVarInt(Integer i) throws IOException {
		if (i == null) {
			write(1);
			return;
		}

		write(0);
		writeVarInt(i);
	}

	public void writeVarLong(long l) throws IOException {
		byte[] encoded = VarLength.encodeLong(l);
		write(encoded);
	}

	public void writeNullableVarLong(Long l) throws IOException {
		if (l == null) {
			write(1);
			return;
		}

		write(0);
		writeVarLong(l);
	}

	public void writeVarDouble(double d) throws IOException {
		long longBits = Double.doubleToLongBits(d);
		byte[] encoded = VarLength.encodeLong(longBits);
		write(encoded);
	}

	public void writeNullableVarDouble(Double d) throws IOException {
		if (d == null) {
			write(1);
			return;
		}

		write(0);
		writeVarDouble(d);
	}

	public void writeVarFloat(float f) throws IOException {
		int intBits = Float.floatToIntBits(f);
		byte[] encoded = VarLength.encodeInt(intBits);
		write(encoded);
	}

	public void writeNullableVarFloat(Float f) throws IOException {
		if (f == null) {
			write(1);
			return;
		}

		write(0);
		writeVarFloat(f);
	}

	public void writeVarString(String s) throws IOException {
		Preconditions.checkNotNull(s);

		writeVarInt(s.length());
		writeChars(s);
	}

	public void writeNullableVarString(String s) throws IOException {
		if (s == null) {
			write(1);
			return;
		}

		write(0);
		writeVarString(s);
	}

	public void writeVarBytes(byte[] bytes) throws IOException {
		Preconditions.checkNotNull(bytes);

		writeVarInt(bytes.length);
		write(bytes);
	}

	public void writeNullableVarBytes(byte[] bytes) throws IOException {
		if (bytes == null) {
			write(1);
			return;
		}

		write(0);
		writeVarBytes(bytes);
	}

	public void writeNullableBoolean(Boolean b) throws IOException {
		if (b == null) {
			write(1);
			return;
		}

		write(0);
		writeBoolean(b);
	}
}
