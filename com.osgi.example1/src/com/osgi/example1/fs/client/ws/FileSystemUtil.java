package com.osgi.example1.fs.client.ws;

import java.io.IOException;

import org.origin.common.rest.client.ClientException;

import com.osgi.example1.fs.client.api.FileRef;
import com.osgi.example1.fs.client.api.FileSystem;
import com.osgi.example1.fs.common.FileMetadata;
import com.osgi.example1.fs.common.Path;

public class FileSystemUtil {

	/**
	 * Recursively walk through folders and print out folder path.
	 * 
	 * @param fs
	 * @param path
	 * @param level
	 * @throws ClientException
	 */
	public static void walkFolders(FileSystemWSClient fsClient, Path path, int level) throws ClientException {
		FileMetadata fileMetaData = fsClient.getFileMetadata(path);
		System.out.println(getSpaces(level) + fileMetaData.getName() + " (" + fileMetaData.getPath() + ")");

		if (fileMetaData.isDirectory()) {
			int deeperLevel = level + 1;
			Path[] subPaths = fsClient.listFiles(path);
			for (Path subPath : subPaths) {
				walkFolders(fsClient, subPath, deeperLevel);
			}
		}
	}

	/**
	 * Recursively walk through folders and print out folder path.
	 * 
	 * @param fs
	 * @param file
	 * @param level
	 * @throws IOException
	 */
	public static void walkFolders(FileSystem fs, FileRef file, int level) throws IOException {
		System.out.println(getSpaces(level) + file.getName() + " (" + file.getPath() + ")");
		// System.out.println(getSpaces(level) + file.getName());

		if (file.isDirectory()) {
			int deeperLevel = level + 1;
			FileRef[] subFiles = FileRef.listFiles(file);
			for (FileRef subFile : subFiles) {
				walkFolders(fs, subFile, deeperLevel);
			}
		}
	}

	/**
	 * 
	 * @param level
	 * @return
	 */
	protected static String getSpaces(int level) {
		String spaces = ""; // $NON-NLS-1$
		for (int i = 0; i < level; i++) {
			spaces += "    "; // $NON-NLS-1$
		}
		return spaces;
	}

}
