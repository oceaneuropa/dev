package com.osgi.example1.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.text.MessageFormat;

public class MySeekableByteChannel implements SeekableByteChannel {

	protected static boolean debug = true;

	/**
	 * 
	 * @param channel
	 * @return
	 */
	public static SeekableByteChannel wrap(SeekableByteChannel channel) {
		SeekableByteChannel resultChannel = (channel != null) ? ((channel instanceof MySeekableByteChannel) ? channel : new MySeekableByteChannel(channel)) : null;
		// if (debug) {
		// Printer.println(MessageFormat.format("MySeekableByteChannel.wrap(SeekableByteChannel) channel = ''{0}'' returns SeekableByteChannel [{1}]", new
		// Object[] { channel, resultChannel }));
		// }
		return resultChannel;
	}

	/**
	 * 
	 * @param channel
	 * @return
	 */
	public static SeekableByteChannel _unwrap(SeekableByteChannel channel) {
		SeekableByteChannel resultChannel = (channel instanceof MySeekableByteChannel) ? ((MySeekableByteChannel) channel).delegateChannel : channel;
		// if (debug) {
		// Printer.println(MessageFormat.format("MySeekableByteChannel.unwrap(SeekableByteChannel) channel = ''{0}'' returns SeekableByteChannel [{1}]", new
		// Object[] { channel, resultChannel }));
		// }
		return resultChannel;
	}

	protected final SeekableByteChannel delegateChannel;

	/**
	 * 
	 * @param channel
	 */
	public MySeekableByteChannel(SeekableByteChannel channel) {
		if (debug) {
			Printer.println(MessageFormat.format("new MySeekableByteChannel(SeekableByteChannel) channel = {0}", new Object[] { channel }));
		}
		this.delegateChannel = channel;
	}

	@Override
	public long size() throws IOException {
		long size = this.delegateChannel.size();
		if (debug) {
			Printer.println(MessageFormat.format("MySeekableByteChannel.size() returns [{0}]", new Object[] { size }));
		}
		return size;
	}

	@Override
	public long position() throws IOException {
		long position = this.delegateChannel.position();
		if (debug) {
			Printer.println(MessageFormat.format("MySeekableByteChannel.position() returns [{0}]", new Object[] { position }));
		}
		return position;
	}

	@Override
	public boolean isOpen() {
		boolean isOpen = this.delegateChannel.isOpen();
		if (debug) {
			Printer.println(MessageFormat.format("MySeekableByteChannel.isOpen() returns [{0}]", new Object[] { isOpen }));
		}
		return isOpen;
	}

	/**
	 * Reads a sequence of bytes from this channel into the given buffer.
	 * 
	 * Bytes are read starting at this channel's current position, and then the position is updated with the number of bytes actually read. Otherwise this
	 * method behaves exactly as specified in the ReadableByteChannel interface.
	 * 
	 * Specified by: read(...) in ReadableByteChannel.
	 * 
	 * @param dst
	 *            The buffer into which bytes are to be transferred
	 * @return The number of bytes read, possibly zero, or -1 if the channel has reached end-of-stream
	 * @throws IOException
	 *             IOException - If some other I/O error occurs
	 */
	@Override
	public int read(ByteBuffer dst) throws IOException {
		// The number of bytes read, possibly zero, or -1 if the channel has reached end-of-stream
		int read = this.delegateChannel.read(dst);
		if (debug) {
			Printer.println(MessageFormat.format("MySeekableByteChannel.read(ByteBuffer) dst = {0} returns int [{1}]", new Object[] { dst, read }));
		}
		return read;
	}

	/**
	 * Writes a sequence of bytes to this channel from the given buffer.
	 * 
	 * Bytes are written starting at this channel's current position, unless the channel is connected to an entity such as a file that is opened with the APPEND
	 * option, in which case the position is first advanced to the end. The entity to which the channel is connected is grown, if necessary, to accommodate the
	 * written bytes, and then the position is updated with the number of bytes actually written. Otherwise this method behaves exactly as specified by the
	 * WritableByteChannel interface.
	 * 
	 * Specified by: write(...) in WritableByteChannel.
	 * 
	 * @param src
	 *            The buffer from which bytes are to be retrieved
	 * @return The number of bytes written, possibly zero
	 * @throws IOException
	 *             Throws: IOException - If some other I/O error occurs
	 */
	@Override
	public int write(ByteBuffer src) throws IOException {
		// The number of bytes written, possibly zero
		int write = this.delegateChannel.write(src);
		if (debug) {
			Printer.println(MessageFormat.format("MySeekableByteChannel.write(ByteBuffer) src = {0} returns int [{1}]", new Object[] { src, write }));
		}
		return write;
	}

	@Override
	public SeekableByteChannel position(long newPosition) throws IOException {
		SeekableByteChannel channel = this.delegateChannel.position(newPosition);
		SeekableByteChannel newChannel = wrap(channel);
		if (debug) {
			Printer.println(MessageFormat.format("MySeekableByteChannel.position(long) newPosition = {0} returns SeekableByteChannel [{1}]", new Object[] { newPosition, newChannel }));
		}
		return newChannel;
	}

	@Override
	public SeekableByteChannel truncate(long size) throws IOException {
		SeekableByteChannel channel = this.delegateChannel.truncate(size);
		SeekableByteChannel newChannel = wrap(channel);
		if (debug) {
			Printer.println(MessageFormat.format("MySeekableByteChannel.truncate(long) size = {0} returns SeekableByteChannel [{1}]", new Object[] { size, newChannel }));
		}
		return newChannel;
	}

	@Override
	public void close() throws IOException {
		this.delegateChannel.close();
		if (debug) {
			Printer.println(MessageFormat.format("MySeekableByteChannel.close()", new Object[] {}));
		}
	}

}
