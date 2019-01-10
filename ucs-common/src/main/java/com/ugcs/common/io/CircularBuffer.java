package com.ugcs.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class CircularBuffer {
	private byte[] buffer;
	private int head; // first element (exists)
	private int tail; // first element to insert (doesn't exist)
	
	private final InputStream in = new InputStreamAdapter();
	private final OutputStream out = new OutputStreamAdapter();

	public CircularBuffer() {
		this(32);
	}
	
	public CircularBuffer(int capacity) {
		if (capacity < 0)
			throw new IllegalArgumentException("capacity");
		
		// keep one slot open for distinction 
		// between empty and full states
		this.buffer = new byte[capacity + 1];
		this.head = 0;
		this.tail = this.head;
	}
	
	public byte[] array() {
		return buffer;
	}
	
	private byte put(int index, byte value) {
		return buffer[index] = value;
	}
	
	private byte get(int index) {
		return buffer[index];
	}
	
	private boolean isEmpty() {
		return tail == head;
	}
	
	private void reserve(int capacity) {
		int length = buffer.length;
		int newLength = capacity + 1;
		
		if (newLength > length) {
			newLength = Math.max(length + (length >> 1), newLength);
			buffer = Arrays.copyOf(buffer, newLength);
			// overlapping case  
			if (tail < head) {
				System.arraycopy(buffer, head, buffer, head + (newLength - length), length - head);
				head += newLength - length;
			}
		}
	}
	
	private int offset(int n) {
		if (n < 0)
			throw new IllegalArgumentException("n");
		if (n > length())
			throw new IndexOutOfBoundsException("underflow error");
		
		return head > tail ? (head + n) % buffer.length : head + n;
	}
	
	public int length() {
		return tail - head + (head > tail ? buffer.length : 0);
	}
	
	public int capacity() {
		return buffer.length;
	}
	
	public byte readByte() {
		if (isEmpty())
			throw new IndexOutOfBoundsException("underflow error");

		byte result = get(head);
		++head;
		if (head == buffer.length)
			head = 0; // wrap around
		return result;
	}
	
	public void writeByte(byte b) {
		reserve(length() + 1);
		// check: buffer is full (if now auto-growth)
		put(tail, b);
		++tail;
		if (tail == buffer.length)
			tail = 0; // wrap around
	}
	
	public InputStream getInputStream() {
		return in;
	}
	
	public OutputStream getOutputStream() {
		return out;
	}
	
	/* InputStream */
	
	private class InputStreamAdapter extends InputStream { 
		private int headMark = -1;
		private int tailMark = -1;
		private int readLimit;
		
		@Override
		public int read() {
			if (isEmpty())
				return -1;
			return readByte() & 0xff;
		}
	
		@Override
		public int read(byte[] b) {
			if (b == null)
				throw new NullPointerException();
	
			return read(b, 0, b.length);
		}
	
		@Override
		public int read(byte[] b, int off, int len) {
			if (b == null)
				throw new NullPointerException();
			if (off < 0 || len < 0 || off + len > b.length)
				throw new IndexOutOfBoundsException();
			
			if (isEmpty())
				return -1;
			int n = 0;
			for (int i = off; i < off + len; ++i) {
				int value = read();
				if (value == -1)
					break;
				b[i] = (byte)value;
				++n;
			}
			return n;
		}
		
		@Override
		public long skip(long n) {
			int tmp = Math.min(available(), Math.max(0, (int)n));
			head = offset(tmp);
			return tmp;
		}
		
		@Override
		public int available() {
			return length();
		}
		
		@Override
		public void mark(int readLimit) {
			headMark = head;
			tailMark = tail;
			
			this.readLimit = readLimit;
		}
	
		@Override
		public void reset() throws IOException {
			if (headMark == -1 || tailMark == -1)
				throw new IOException("mark has not been set");
			int n = head - headMark;
			if (n < 0)
				n += buffer.length;
			if (n > readLimit)
				throw new IOException("mark has been invalidated");
			
			head = headMark;
			tail = tailMark;
		}
	
		@Override
		public boolean markSupported() {
			return true;
		}
	}
	
	/* OutputStream */
	
	private class OutputStreamAdapter extends OutputStream {
	
		@Override
		public void write(int b) {
			writeByte((byte)b);
		}
	
		@Override
		public void write(byte[] b) {
			if (b == null)
				throw new NullPointerException();
	
			write(b, 0, b.length);
		}
	
		@Override
		public void write(byte[] b, int off, int len) {
			if (b == null)
				throw new NullPointerException();
			if (off < 0 || len < 0 || off + len > b.length)
				throw new IndexOutOfBoundsException();
		
			if (len == 0)
				return;
			
			reserve(length() + len);
			for (int i = off; i < off + len; ++i) {
				put(tail, b[i]);
				++tail;
				if (tail == buffer.length)
					tail = 0; // wrap around
			}
		}
	}
}
