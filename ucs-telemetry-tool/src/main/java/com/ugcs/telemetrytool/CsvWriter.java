package com.ugcs.telemetrytool;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

import com.ugcs.common.util.Preconditions;

public class CsvWriter implements Closeable, Flushable{
	private final OutputStream out;
	private static final char SEPARATOR = ',';
	private static final char QUOTE_CHAR = '\"';
	private static final char LINE_END = '\n';

	public CsvWriter(OutputStream out) {
		Preconditions.checkNotNull(out);

		this.out = out;
	}

	public void writeNext(String[] nextLine) throws IOException {
		Appendable appendable = new StringBuilder(1024);
		if (nextLine != null) {
			for (int i = 0; i < nextLine.length; ++i) {
				if (i != 0) {
					appendable.append(SEPARATOR);
				}

				String nextElement = nextLine[i];
				if (nextElement != null) {
					if (nextElement.indexOf(QUOTE_CHAR) != -1) {
						nextElement = nextElement.replaceAll(String.valueOf(QUOTE_CHAR), "\"\"");
					}
					if (nextElement.indexOf(SEPARATOR) != -1 || nextElement.indexOf(LINE_END) != -1) {
						appendable.append(QUOTE_CHAR);
						appendable.append(nextElement);
						appendable.append(QUOTE_CHAR);
					} else {
						appendable.append(nextElement);
					}
				}
			}

			appendable.append(LINE_END);
			this.out.write(appendable.toString().getBytes());
		}
	}

	@Override
	public void close() throws IOException {
		this.flush();
		out.close();
	}

	@Override
	public void flush() throws IOException {
		out.flush();
	}
}