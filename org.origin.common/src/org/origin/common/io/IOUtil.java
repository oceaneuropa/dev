package org.origin.common.io;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.URI;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

public class IOUtil {
	/**
	 * Represents the end-of-file (or stream).
	 * 
	 * @since 2.5 (made public)
	 */
	public static final int EOF = -1;

	/**
	 * The Unix directory separator character.
	 */
	public static final char DIR_SEPARATOR_UNIX = '/';
	/**
	 * The Windows directory separator character.
	 */
	public static final char DIR_SEPARATOR_WINDOWS = '\\';
	/**
	 * The system directory separator character.
	 */
	public static final char DIR_SEPARATOR = File.separatorChar;
	/**
	 * The Unix line separator string.
	 */
	public static final String LINE_SEPARATOR_UNIX = "\n";
	/**
	 * The Windows line separator string.
	 */
	public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
	/**
	 * The system line separator string.
	 */
	public static final String LINE_SEPARATOR;

	static {
		// avoid security issues
		final StringBuilderWriter buf = new StringBuilderWriter(4);
		final PrintWriter out = new PrintWriter(buf);
		out.println();
		LINE_SEPARATOR = buf.toString();
		out.close();
	}

	public static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

	public static final String DEFAULT_CHARSET_NAME = Charset.defaultCharset().name();

	// ------------------------------------------------------------------------------------
	// Read
	// ------------------------------------------------------------------------------------
	/**
	 * Gets the contents of an InputStream as a byte[].
	 * 
	 * This method buffers the input internally, so there is no need to use a BufferedInputStream.
	 *
	 * @param input
	 *            the InputStream to read from
	 * @return the requested byte array
	 * @throws NullPointerException
	 *             if the input is null
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static byte[] toByteArray(InputStream input) throws IOException {
		return org.apache.commons.io.IOUtils.toByteArray(input);
	}

	/**
	 * Gets the contents of a URI as a byte[].
	 *
	 * @param uri
	 *            the URI to read
	 * @return the requested byte array
	 * @throws NullPointerException
	 *             if the uri is null
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	public static byte[] toByteArray(URI uri) throws IOException {
		return org.apache.commons.io.IOUtils.toByteArray(uri);
	}

	/**
	 * Gets the contents of a URLConnection as a byte[].
	 *
	 * @param urlConn
	 *            the URLConnection to read
	 * @return the requested byte array
	 * @throws NullPointerException
	 *             if the urlConn is null
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	public static byte[] toByteArray(URLConnection urlConn) throws IOException {
		return org.apache.commons.io.IOUtils.toByteArray(urlConn);
	}

	/**
	 * Gets the contents of a Reader as a byte[] using the specified character encoding.
	 * 
	 * Character encoding names can be found at http://www.iana.org/assignments/character-sets.
	 * 
	 * This method buffers the input internally, so there is no need to use a BufferedReader.
	 *
	 * @param input
	 *            the Reader to read from
	 * @param encoding
	 *            the encoding to use, null means platform default
	 * @return the requested byte array
	 * @throws NullPointerException
	 *             if the input is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws java.nio.charset.UnsupportedCharsetException
	 *             thrown if the encoding is not supported.
	 */
	public static byte[] toByteArray(Reader input, String encoding) throws IOException {
		return org.apache.commons.io.IOUtils.toByteArray(input, encoding);
	}

	/**
	 * Gets the contents of an InputStream as a list of Strings, one entry per line, using the specified character encoding.
	 * 
	 * Character encoding names can be found at http://www.iana.org/assignments/character-sets.
	 * 
	 * This method buffers the input internally, so there is no need to use a BufferedInputStream.
	 *
	 * @param input
	 *            the InputStream to read from, not null
	 * @param encoding
	 *            the encoding to use, null means platform default
	 * @return the list of Strings, never null
	 * @throws NullPointerException
	 *             if the input is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws java.nio.charset.UnsupportedCharsetException
	 *             thrown if the encoding is not supported.
	 */
	public static List<String> readLines(InputStream input, String encoding) throws IOException {
		return org.apache.commons.io.IOUtils.readLines(input, encoding);
	}

	/**
	 * Gets the contents of a Reader as a list of Strings, one entry per line.
	 * 
	 * This method buffers the input internally, so there is no need to use a BufferedReader.
	 *
	 * @param input
	 *            the Reader to read from, not null
	 * @return the list of Strings, never null
	 * @throws NullPointerException
	 *             if the input is null
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static List<String> readLines(Reader input) throws IOException {
		return org.apache.commons.io.IOUtils.readLines(input);
	}

	// ------------------------------------------------------------------------------------
	// Write
	// ------------------------------------------------------------------------------------
	/**
	 * Writes the toString() value of each item in a collection to an OutputStream line by line, using the specified character encoding and the
	 * specified line ending.
	 * 
	 * Character encoding names can be found at http://www.iana.org/assignments/character-sets.
	 *
	 * @param lines
	 *            the lines to write, null entries produce blank lines
	 * @param lineEnding
	 *            the line separator to use, null is system default
	 * @param output
	 *            the OutputStream to write to, not null, not closed
	 * @param encoding
	 *            the encoding to use, null means platform default
	 * @throws NullPointerException
	 *             if the output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws java.nio.charset.UnsupportedCharsetException
	 *             thrown if the encoding is not supported.
	 */
	public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, String encoding) throws IOException {
		org.apache.commons.io.IOUtils.writeLines(lines, lineEnding, output, encoding);
	}

	/**
	 * Writes the toString() value of each item in a collection to a Writer line by line, using the specified line ending.
	 *
	 * @param lines
	 *            the lines to write, null entries produce blank lines
	 * @param lineEnding
	 *            the line separator to use, null is system default
	 * @param writer
	 *            the Writer to write to, not null, not closed
	 * @throws NullPointerException
	 *             if the input is null
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static void writeLines(Collection<?> lines, String lineEnding, Writer writer) throws IOException {
		// org.apache.commons.io.IOUtils.writeLines(lines, lineEnding, writer);

		if (lines == null) {
			return;
		}
		if (lineEnding == null) {
			lineEnding = LINE_SEPARATOR;
		}
		for (final Object line : lines) {
			if (line != null) {
				writer.write(line.toString());
			}
			writer.write(lineEnding);
		}
	}

	// ------------------------------------------------------------------------------------
	// Convert
	// ------------------------------------------------------------------------------------
	/**
	 * Converts the specified string to an input stream, encoded as bytes using the specified character encoding.
	 * 
	 * Character encoding names can be found at http://www.iana.org/assignments/character-sets.
	 *
	 * @param input
	 *            the string to convert
	 * @param encoding
	 *            the encoding to use, null means platform default
	 * @return an input stream
	 * @throws IOException
	 *             if the encoding is invalid
	 * @throws java.nio.charset.UnsupportedCharsetException
	 *             thrown if the encoding is not supported.
	 */
	public static InputStream toInputStream(String input, String encoding) throws IOException {
		// return org.apache.commons.io.IOUtils.toInputStream(input, encoding);

		byte[] bytes = input.getBytes(Charsets.toCharset(encoding));
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * Converts the specified bytes[] to an input stream.
	 * 
	 * @param bytes
	 *            the bytes[] to convert
	 * @return
	 */
	public static InputStream toInputStream(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * Gets the contents of an <code>InputStream</code> as a String using the specified character encoding.
	 * 
	 * Character encoding names can be found at http://www.iana.org/assignments/character-sets.
	 * 
	 * This method buffers the input internally, so there is no need to use a BufferedInputStream.
	 *
	 * @param input
	 *            the InputStream to read from
	 * @param encoding
	 *            the encoding to use, null means platform default
	 * @return the requested String
	 * @throws NullPointerException
	 *             if the input is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws java.nio.charset.UnsupportedCharsetException
	 *             thrown if the encoding is not supported.
	 */
	public static String toString(InputStream input, String encoding) throws IOException {
		return org.apache.commons.io.IOUtils.toString(input, encoding);
	}

	/**
	 * Gets the contents of a byte[] as a String using the specified character encoding.
	 * 
	 * Character encoding names can be found at http://www.iana.org/assignments/character-sets.
	 *
	 * @param input
	 *            the byte array to read from
	 * @param encoding
	 *            the encoding to use, null means platform default
	 * @return the requested String
	 * @throws NullPointerException
	 *             if the input is null
	 * @throws IOException
	 *             if an I/O error occurs (never occurs)
	 */
	public static String toString(final byte[] input, final String encoding) throws IOException {
		// return org.apache.commons.io.IOUtils.toString(input, encoding);

		return new String(input, Charsets.toCharset(encoding));
	}

	// ------------------------------------------------------------------------------------
	// Copy
	// ------------------------------------------------------------------------------------
	/**
	 * Copies bytes from an InputStream to an OutputStream.
	 * 
	 * This method buffers the input internally, so there is no need to use a BufferedInputStream.
	 * 
	 * Large streams (over 2GB) will return a bytes copied value of -1 after the copy has completed since the correct number of bytes cannot be
	 * returned as an int. For large streams use the copyLarge(InputStream, OutputStream) method.
	 *
	 * @param input
	 *            the InputStream to read from
	 * @param output
	 *            the OutputStream to write to
	 * @return the number of bytes copied, or -1 if greater than Integer.MAX_VALUE
	 * @throws NullPointerException
	 *             if the input or output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static int copy(InputStream input, OutputStream output) throws IOException {
		return org.apache.commons.io.IOUtils.copy(input, output);
	}

	/**
	 * Copies bytes from a large (over 2GB) InputStream to an OutputStream.
	 * 
	 * This method buffers the input internally, so there is no need to use a BufferedInputStream.
	 *
	 * @param input
	 *            the InputStream to read from
	 * @param output
	 *            the OutputStream to write to
	 * @return the number of bytes copied
	 * @throws NullPointerException
	 *             if the input or output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static long copyLarge(InputStream input, OutputStream output) throws IOException {
		return org.apache.commons.io.IOUtils.copyLarge(input, output);
	}

	/**
	 * Copies chars from a Reader to a Writer.
	 * 
	 * This method buffers the input internally, so there is no need to use a BufferedReader.
	 * 
	 * Large streams (over 2GB) will return a chars copied value of -1 after the copy has completed since the correct number of chars cannot be
	 * returned as an int. For large streams use the copyLarge(Reader, Writer) method.
	 *
	 * @param input
	 *            the Reader to read from
	 * @param output
	 *            the Writer to write to
	 * @return the number of characters copied, or -1 if greater than Integer.MAX_VALUE
	 * @throws NullPointerException
	 *             if the input or output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static int copy(Reader input, Writer output) throws IOException {
		return org.apache.commons.io.IOUtils.copy(input, output);
	}

	/**
	 * Copies chars from a large (over 2GB) Reader to a Writer.
	 * 
	 * This method buffers the input internally, so there is no need to use a BufferedReader.
	 *
	 * @param input
	 *            the Reader to read from
	 * @param output
	 *            the Writer to write to
	 * @return the number of characters copied
	 * @throws NullPointerException
	 *             if the input or output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static long copyLarge(Reader input, Writer output) throws IOException {
		return org.apache.commons.io.IOUtils.copyLarge(input, output);
	}

	/**
	 * Copies bytes from an InputStream to chars on a Writer using the specified character encoding.
	 * 
	 * This method buffers the input internally, so there is no need to use a BufferedInputStream.
	 * 
	 * Character encoding names can be found at http://www.iana.org/assignments/character-sets.
	 * 
	 * This method uses {@link InputStreamReader}.
	 *
	 * @param input
	 *            the InputStream to read from
	 * @param output
	 *            the Writer to write to
	 * @param inputEncoding
	 *            the encoding to use for the InputStream, null means platform default
	 * @throws NullPointerException
	 *             if the input or output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws java.nio.charset.UnsupportedCharsetException
	 *             thrown if the encoding is not supported.
	 */
	public static void copy(InputStream input, Writer output, String inputEncoding) throws IOException {
		org.apache.commons.io.IOUtils.copy(input, output, inputEncoding);
	}

	/**
	 * Copies chars from a Reader to bytes on an OutputStream using the specified character encoding, and calling flush.
	 * 
	 * This method buffers the input internally, so there is no need to use a BufferedReader.
	 * 
	 * Character encoding names can be found at http://www.iana.org/assignments/character-sets.
	 * 
	 * Due to the implementation of OutputStreamWriter, this method performs a flush.
	 * 
	 * This method uses OutputStreamWriter.
	 *
	 * @param input
	 *            the Reader to read from
	 * @param output
	 *            the OutputStream to write to
	 * @param outputEncoding
	 *            the encoding to use for the OutputStream, null means platform default
	 * @throws NullPointerException
	 *             if the input or output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws java.nio.charset.UnsupportedCharsetException
	 *             thrown if the encoding is not supported.
	 */
	public static void copy(Reader input, OutputStream output, String outputEncoding) throws IOException {
		org.apache.commons.io.IOUtils.copy(input, output, outputEncoding);
	}

	// ------------------------------------------------------------------------------------
	// Close
	// ------------------------------------------------------------------------------------
	/**
	 * Closes a Closeable unconditionally.
	 * 
	 * Equivalent to Closeable#close(), except any exceptions will be ignored. This is typically used in finally blocks.
	 * 
	 * @param closeable
	 *            a Closeable to be closed.
	 * @param printStackTrace
	 *            if printStackTrace is false, any exceptions will be ignored. if printStackTrace is true, if any exceptions occurs, it will be
	 *            printed out.
	 */
	public static void closeQuietly(Closeable closeable, boolean printStackTrace) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			if (printStackTrace) {
				ioe.printStackTrace();
			} else {
				// ignore
			}
		}
	}

	/**
	 * Close the Closeable objects and <b>ignore</b> any {@link IOException} or null pointers. Must only be used for cleanup in exception handlers.
	 *
	 * @param log
	 *            the log to record problems to at debug level. Can be null.
	 * @param closeables
	 *            the objects to close
	 */
	public static void closeQuietly(boolean printStackTrace, Closeable... closeables) {
		for (java.io.Closeable c : closeables) {
			if (c != null) {
				try {
					c.close();
				} catch (IOException e) {
					if (printStackTrace) {
						e.printStackTrace();
					} else {
						// ignore
					}
				}
			}
		}
	}

	/**
	 * @see org.apache.commons.io.output.StringBuilderWriter
	 *
	 */
	public static class StringBuilderWriter extends Writer implements Serializable {

		private static final long serialVersionUID = 5122517056858241663L;
		private final StringBuilder builder;

		public StringBuilderWriter() {
			this.builder = new StringBuilder();
		}

		public StringBuilderWriter(final int capacity) {
			this.builder = new StringBuilder(capacity);
		}

		/**
		 * Constructs a new instance with the specified {@link StringBuilder}.
		 * 
		 * <p>
		 * If {@code builder} is null a new instance with default capacity will be created.
		 * </p>
		 *
		 * @param builder
		 *            The String builder. May be null.
		 */
		public StringBuilderWriter(final StringBuilder builder) {
			this.builder = builder != null ? builder : new StringBuilder();
		}

		/**
		 * Appends a single character to this Writer.
		 *
		 * @param value
		 *            The character to append
		 * @return This writer instance
		 */
		@Override
		public Writer append(final char value) {
			builder.append(value);
			return this;
		}

		/**
		 * Appends a character sequence to this Writer.
		 *
		 * @param value
		 *            The character to append
		 * @return This writer instance
		 */
		@Override
		public Writer append(final CharSequence value) {
			builder.append(value);
			return this;
		}

		/**
		 * Appends a portion of a character sequence to the {@link StringBuilder}.
		 *
		 * @param value
		 *            The character to append
		 * @param start
		 *            The index of the first character
		 * @param end
		 *            The index of the last character + 1
		 * @return This writer instance
		 */
		@Override
		public Writer append(final CharSequence value, final int start, final int end) {
			builder.append(value, start, end);
			return this;
		}

		/**
		 * Closing this writer has no effect.
		 */
		@Override
		public void close() {
		}

		/**
		 * Flushing this writer has no effect.
		 */
		@Override
		public void flush() {
		}

		/**
		 * Writes a String to the {@link StringBuilder}.
		 * 
		 * @param value
		 *            The value to write
		 */
		@Override
		public void write(final String value) {
			if (value != null) {
				builder.append(value);
			}
		}

		/**
		 * Writes a portion of a character array to the {@link StringBuilder}.
		 *
		 * @param value
		 *            The value to write
		 * @param offset
		 *            The index of the first character
		 * @param length
		 *            The number of characters to write
		 */
		@Override
		public void write(final char[] value, final int offset, final int length) {
			if (value != null) {
				builder.append(value, offset, length);
			}
		}

		/**
		 * @return The underlying builder.
		 */
		public StringBuilder getBuilder() {
			return builder;
		}

		/**
		 * @return The contents of the String builder.
		 */
		@Override
		public String toString() {
			return builder.toString();
		}
	}

	/**
	 * @see org.apache.commons.io.Charsets
	 * 
	 */
	public static class Charsets {
		/**
		 * Returns the given Charset or the default Charset if the given Charset is null.
		 * 
		 * @param charset
		 *            A charset or null.
		 * @return the given Charset or the default Charset if the given Charset is null
		 */
		public static Charset toCharset(final Charset charset) {
			return charset == null ? Charset.defaultCharset() : charset;
		}

		/**
		 * Returns a Charset for the named charset. If the name is null, return the default Charset.
		 * 
		 * @param charset
		 *            The name of the requested charset, may be null.
		 * @return a Charset for the named charset
		 * @throws java.nio.charset.UnsupportedCharsetException
		 *             If the named charset is unavailable
		 */
		public static Charset toCharset(final String charset) {
			return charset == null ? Charset.defaultCharset() : Charset.forName(charset);
		}
	}

}
