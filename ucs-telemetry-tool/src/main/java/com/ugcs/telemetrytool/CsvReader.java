package com.ugcs.telemetrytool;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// RFC 4180
public class CsvReader {
	private static final char SEPARATOR = ',';
	private static final char QUOTE = '"';
	
	private final Reader reader;
	private final char[] buffer;
	private int length;
	private int offset;
	private char[] fieldBuffer = new char[1024];

	public CsvReader(Reader reader) {
		this (reader, 65536);
	}
	
	public CsvReader(Reader reader, int size) {
		if (reader == null)
			throw new IllegalArgumentException("reader");
		if (size <= 0)
			throw new IllegalArgumentException("size");
		
		this.reader = reader;
		this.buffer = new char[size];
		this.length = 0;
		this.offset = 0;
	}
	
	private char readChar() throws IOException {
		if (offset == length) {
			// refill buffer
			length = reader.read(buffer, 0, buffer.length);
			offset = 0;
		}
		if (length == -1)
			return 0;
		return buffer[offset++];
	}
	
	public List<String> readFields() throws IOException {
		if (length == -1)
			return null;
		
		List<String> result = new ArrayList<>();
		int fieldLength = 0;
		
		boolean quoted = false;
		while (length != -1) {
			char c = readChar();
			if (length == -1)
				break;
			if (!quoted) {
				if (c == 0x0d) {
					// skip LF, if necessary
					char c1 = readChar(); 
					if (length == -1)
						break;
					if (c1 != 0x0a)
						offset -= 1;
					break;
				}
				if (c == 0x0a)
					break;
				if (c == SEPARATOR) {
					// next field
					result.add(new String(fieldBuffer, 0, fieldLength));
					fieldLength = 0;
					continue;
				}
				if (c == QUOTE) {
					quoted = true;
					continue;
				}
			} else {
				if (c == QUOTE) {
					// check if quote is escaped
					char c1 = readChar();
					if (length == -1)
						break;
					if (c1 != QUOTE) {
						quoted = false;
						offset -= 1;
						continue;
					}
				}
			}
			if (fieldLength + 1 >= fieldBuffer.length) {
				fieldBuffer = Arrays.copyOf(fieldBuffer, 
						Math.max(fieldBuffer.length + (fieldBuffer.length >> 1), fieldLength + 1));
			}
			fieldBuffer[fieldLength++] = c;
		}
		result.add(new String(fieldBuffer, 0, fieldLength));
		return result;
	}
	
	public void close() throws IOException {
		reader.close();
	}
}
