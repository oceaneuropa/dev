package com.osgi.example1.nio;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 2) Read a large file in chunks with fixed size buffer
 * 
 * @see http://howtodoinjava.com/java-7/nio/3-ways-to-read-files-using-java-nio/
 * 
 */
public class ReadFileWithFixedSizeBuffer {

	public static void main(String[] args) {
		try {
			File file = new File("test.txt");
			System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());

			RandomAccessFile aFile = new RandomAccessFile("test.txt", "r");
			FileChannel inChannel = aFile.getChannel();

			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (inChannel.read(buffer) > 0) {
				buffer.flip();
				for (int i = 0; i < buffer.limit(); i++) {
					System.out.print((char) buffer.get());
				}
				buffer.clear(); // do something with the data and clear/compact it.
			}

			inChannel.close();
			aFile.close();

		} catch (Exception e) {
			e.getMessage();
		}
	}

}