package com.osgi.example1.nio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyFiles {

	// buffer size used for reading and writing
	private static final int BUFFER_SIZE = 8192;

	/**
	 * Reads all bytes from an input stream and writes them to an output stream.
	 * 
	 * @param source
	 * @param sink
	 * @return
	 * @throws IOException
	 */
	public static long copy(InputStream source, OutputStream sink) throws IOException {
		long nread = 0L;
		byte[] buf = new byte[BUFFER_SIZE];
		int n;
		while ((n = source.read(buf)) > 0) {
			sink.write(buf, 0, n);
			nread += n;
		}
		return nread;
	}

}
