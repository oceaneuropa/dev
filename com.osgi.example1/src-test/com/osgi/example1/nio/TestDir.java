package com.osgi.example1.nio;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @see http://www.rgagnon.com/javadetails/java-get-directory-content-faster-with-many-files.html
 * @see http://howtodoinjava.com/best-practices/java-8-list-all-files-example/
 *
 */
public class TestDir {

	/**
	 * The classical way
	 * 
	 * @param filePath
	 * @param maxFiles
	 * @throws IOException
	 */
	private static void ioRun(String filePath, int maxFiles) throws IOException {
		int i = 1;
		System.out.println("IO run");
		long start = System.currentTimeMillis();
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();
		// System.out.println("Total : " + listOfFiles.length);
		for (File file : listOfFiles) {
			System.out.println("" + i + ": " + file.getName());
			if (++i > maxFiles) {
				break;
			}
		}
		long stop = System.currentTimeMillis();
		System.out.println("Elapsed: " + (stop - start) + " ms");
	}

	/**
	 * The new way
	 * 
	 * @param filePath
	 * @param maxFiles
	 * @throws IOException
	 */
	private static void nioRun(String filePath, int maxFiles) throws IOException {
		int i = 1;
		System.out.println("NIO run");
		long start = System.currentTimeMillis();
		Path dir = FileSystems.getDefault().getPath(filePath);
		DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
		for (Path path : stream) {
			System.out.println("" + i + ": " + path.getFileName());
			if (++i > maxFiles) {
				break;
			}
		}
		stream.close();
		long stop = System.currentTimeMillis();
		System.out.println("Elapsed: " + (stop - start) + " ms");
	}

	public static void main(String[] args) {
		try {
			// String filePath = "\\\\server.local\\files\\20130220";
			String filePath = "/Users/oceaneuropa";
			int maxFiles = 10;
			System.out.println("TEST BIG DIR");
			nioRun(filePath, maxFiles);
			ioRun(filePath, maxFiles);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
