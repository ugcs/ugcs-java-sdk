package com.ugcs.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.ugcs.common.util.Preconditions;

public final class Bytes {
	private static final int COPY_BUFFER_LENGTH = 1024 * 64;
	
	private Bytes() {
		// forbidden
	}
	
	public static long copy(InputStream in, OutputStream out) throws IOException {
		byte[] copyBuffer = new byte[COPY_BUFFER_LENGTH];
		int n = 0;
		long total = 0;
		while ((n = in.read(copyBuffer)) != -1) {
			out.write(copyBuffer, 0, n);
			total += n;
		}
		return total;
	}

	public static byte[] getBytes(InputStream in) throws IOException {
		ArrayOutputStream out =
				new ArrayOutputStream(in.available());
		try {
			Bytes.copy(in, out);
			out.flush();
			return out.toBytes();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	public static int compareByteArray(byte[] one, byte[] two) {
		Preconditions.checkNotNull(one);
		Preconditions.checkNotNull(two);
		if (one.length != two.length)
			return Integer.compare(one.length, two.length);
		for (int i = 0; i < one.length; i++) {
			if (one[i] != two[i])
				return Byte.compare(one[i], two[i]);
		}
		return 0;
	}
	
	public static String getString(InputStream in, Charset charset) throws IOException {
		byte[] buffer = getBytes(in);
		return new String(buffer, charset);
	}
	
	public static String getString(InputStream in) throws IOException {
		return getString(in, Charset.defaultCharset());
	}
}
