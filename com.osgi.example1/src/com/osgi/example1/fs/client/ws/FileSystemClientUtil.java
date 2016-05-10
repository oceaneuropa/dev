package com.osgi.example1.fs.client.ws;

import org.origin.common.rest.client.ClientException;

import com.osgi.example1.fs.common.FileMetadata;
import com.osgi.example1.fs.common.Path;

public class FileSystemClientUtil {

	/**
	 * Recursively walk through folders and print out folder path.
	 * 
	 * @param fs
	 * @param path
	 * @param level
	 * @throws ClientException
	 */
	public static void walkFolders(FileSystemClient fsClient, Path path, int level) throws ClientException {
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
