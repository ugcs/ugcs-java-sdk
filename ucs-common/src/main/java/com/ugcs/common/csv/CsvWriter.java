package com.ugcs.common.csv;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

import com.ugcs.common.util.Preconditions;

public class CsvWriter implements Closeable, Flushable {

	private final Writer writer;

	public CsvWriter(Writer writer) {
		Preconditions.checkNotNull(writer);

		this.writer = writer;
	}

	public void writeFields(String[] fields) throws IOException {
		if (fields == null)
			return;
		for (int i = 0; i < fields.length; ++i) {
			String field = fields[i];
			if (field != null) {
				if (field.indexOf(Csv.QUOTE_CHAR) != -1) {
					field = field.replaceAll(
							String.valueOf(Csv.QUOTE_CHAR),
							Csv.ESCAPED_QUOTE_STRING);
				}
				boolean quote = field.indexOf(Csv.SEPARATOR) != -1
						|| field.indexOf(Csv.LINE_END) != -1;
				if (quote)
					writer.write(Csv.QUOTE_CHAR);
				writer.write(field);
				if (quote)
					writer.write(Csv.QUOTE_CHAR);
			}
			if (i < fields.length - 1)
				writer.write(Csv.SEPARATOR);
		}
		writer.write(Csv.LINE_END);
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}
}