package com.ugcs.telemetrytool;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

public class CsvWriter implements Closeable, Flushable{
	private final Writer writer;
	private static final char SEPARATOR = ',';
	private static final char QUOTE_CHAR = '\"';
	private static final char LINE_END = '\n';

	public CsvWriter(Writer writer) {
		this.writer = writer;
	}

	public void writeNext(String[] nextLine) throws IOException {
		Appendable appendable = new StringBuilder(1024);
		if(nextLine != null) {
			for(int i = 0; i < nextLine.length; ++i) {
				if(i != 0) {
					appendable.append(SEPARATOR);
				}

				String nextElement = nextLine[i];
				if(nextElement != null) {
					if (nextElement.indexOf(QUOTE_CHAR) != -1) {
						nextElement = nextElement.replaceAll("\"", "\"\"");
					}
					if(nextElement.indexOf(SEPARATOR) != -1 || nextElement.indexOf(LINE_END) != -1) {
						appendable.append(QUOTE_CHAR);
						appendable.append(nextElement);
						appendable.append(QUOTE_CHAR);
					} else {
						appendable.append(nextElement);
					}
				}
			}

			appendable.append(LINE_END);
			this.writer.write(appendable.toString());
		}
	}

	@Override
	public void close() throws IOException {
		this.flush();
		writer.close();
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}
}
