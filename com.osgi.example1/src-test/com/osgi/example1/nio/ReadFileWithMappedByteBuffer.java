package com.osgi.example1.nio;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 3) Faster file copy with mapped byte buffer
 * 
 * @see http://howtodoinjava.com/java-7/nio/3-ways-to-read-files-using-java-nio/
 * 
 */
public class ReadFileWithMappedByteBuffer {

	public static void main(String[] args) {
		try {
			File file = new File("test.txt");
			System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());

			RandomAccessFile aFile = new RandomAccessFile("test.txt", "r");
			FileChannel inChannel = aFile.getChannel();

			MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
			buffer.load();
			for (int i = 0; i < buffer.limit(); i++) {
				System.out.print((char) buffer.get());
			}

			buffer.clear(); // do something with the data and clear/compact it.
			inChannel.close();
			aFile.close();

		} catch (Exception e) {
			e.getMessage();
		}
	}

}
