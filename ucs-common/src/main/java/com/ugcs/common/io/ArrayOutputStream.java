package com.ugcs.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class ArrayOutputStream extends OutputStream {
	private byte[] buffer;
	private int position;
	
	public ArrayOutputStream() {
		buffer = new byte[32];
	}
	
	public ArrayOutputStream(int size) {
		if (size < 0)
			throw new IllegalArgumentException("Negative initial size");
		
		buffer = new byte[size];
	}
	
	private void reserve(int n) {
		if (n > buffer.length) {
			// exponential buffer growth strategy (x 1.5)
			buffer = Arrays.copyOf(buffer, Math.max(buffer.length + (buffer.length >> 1), n));
		}
	}

	@Override
	public void write(int b) throws IOException {
		reserve(position + 1);
		buffer[position] = (byte)b;
		position += 1;
	}

	@Override
	public void write(byte[] b) throws IOException {
		if (b == null)
			throw new NullPointerException();
		
		write(b, 0, b.length);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if (b == null)
			throw new NullPointerException();
		if (off < 0 || len < 0 || off + len > b.length)
			throw new IndexOutOfBoundsException();
	
		if (len == 0)
			return;
		reserve(position + len);
		System.arraycopy(b, off, buffer, position, len);
		position += len;
	}
	
	public byte[] toBytes() {
		return Arrays.copyOf(buffer, position);
	}
	
	public InputStream getInputStream() {
		return new ArrayInputStream(buffer, 0, position);
	}
}
