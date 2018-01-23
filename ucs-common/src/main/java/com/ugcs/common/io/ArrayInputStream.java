package com.ugcs.common.io;

import java.io.IOException;
import java.io.InputStream;

public class ArrayInputStream extends InputStream {
	private byte[] buffer;
	private int offset;
	private int length; // from the offset
	private int position; // from the offset
	private int mark = -1; // not set
	private int readlimit; // mark read limit
	
	public ArrayInputStream(byte[] buffer) {
		if (buffer == null)
			throw new IllegalArgumentException("buffer");
		
		this.buffer = buffer;
		this.offset = 0;
		this.length = buffer.length;
	}
	
	public ArrayInputStream(byte[] buffer, int offset, int length) {
		if (buffer == null)
			throw new IllegalArgumentException("buffer");
		if (offset < 0 || length < 0 || offset + length > buffer.length)
			throw new IndexOutOfBoundsException();
		
		this.buffer = buffer;
		this.offset = offset;
		this.length = length;
	}

	@Override
	public int read() throws IOException {
		return available() > 0 ? buffer[offset + position++] & 0xff : -1;		
	}

	@Override
	public int read(byte[] b) throws IOException {
		if (b == null)
			throw new NullPointerException();

		return read(b, 0, b.length);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (b == null)
			throw new NullPointerException();
		if (off < 0 || len < 0 || off + len > b.length)
			throw new IndexOutOfBoundsException();
		
		if (available() <= 0)
			return len == 0 ? 0 : -1;
		int n = Math.min(available(), len);
		System.arraycopy(buffer, offset + position, b, off, n);
		position += n;
		return n;
	}

	@Override
	public long skip(long n) throws IOException {
		int tmp = Math.min(available(), Math.max(0, (int)n));
		position += tmp;
		return tmp;
	}

	@Override
	public int available() throws IOException {
		return length - position;
	}

	/* mark-reset */
	
	@Override
	public synchronized void mark(int readlimit) {
		mark = position;
		this.readlimit = readlimit;
	}

	@Override
	public synchronized void reset() throws IOException {
		if (mark == -1)
			throw new IOException("mark has not been set");
		if (position - mark > readlimit)
			throw new IOException("mark has been invalidated");
		
		position = mark;
	}

	@Override
	public boolean markSupported() {
		return true;
	}
}
