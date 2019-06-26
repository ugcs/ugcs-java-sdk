package com.ugcs.common.io;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream {

	@Override
	public void write(int b) throws IOException {
		// do nothing
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if (b == null)
			throw new NullPointerException();
		if (off < 0 || len < 0 || off + len > b.length)
			throw new IndexOutOfBoundsException();

		// do nothing;
		// args are checked for the interface conformance
	}
}
