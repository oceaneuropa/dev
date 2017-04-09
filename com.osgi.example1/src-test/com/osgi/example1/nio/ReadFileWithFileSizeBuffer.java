package com.osgi.example1.nio;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 1) Read a small file in buffer of file size
 * 
 * @see http://howtodoinjava.com/java-7/nio/3-ways-to-read-files-using-java-nio/
 * 
 */
public class ReadFileWithFileSizeBuffer {

	public static void main(String args[]) {
		try {
			File file = new File("test.txt");
			System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());

			RandomAccessFile aFile = new RandomAccessFile("test.txt", "r");
			FileChannel inChannel = aFile.getChannel();

			long fileSize = inChannel.size();
			ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
			inChannel.read(buffer);
			// buffer.rewind();
			buffer.flip();
			for (int i = 0; i < fileSize; i++) {
				System.out.print((char) buffer.get());
			}

			inChannel.close();
			aFile.close();

		} catch (Exception e) {
			e.getMessage();
		}
	}

}
