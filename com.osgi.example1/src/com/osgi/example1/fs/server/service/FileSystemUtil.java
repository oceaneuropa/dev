package com.osgi.example1.fs.server.service;

import com.osgi.example1.fs.common.Path;
import com.osgi.example1.fs.common.dto.FileMetaData;

public class FileSystemUtil {

	public static void copyFileToDirectory() {

	}

	/**
	 * Recursively walk through folders and print out folder path.
	 * 
	 * @param fs
	 * @param path
	 * @param level
	 */
	public static void walkFolders(FileSystem fs, Path path, int level) {
		FileMetaData fileMetaData = fs.getFileMetaData(path);
		// System.out.println(getSpaces(level) + "path= " + path.getName());
		// System.out.println(getSpaces(level) + "fileMetaData = " + fileMetaData.toString());
		// System.out.println(getSpaces(level) + path.getName() + " " + fileMetaData.toString());
		System.out.println(getSpaces(level) + fileMetaData.getName() + " (" + fileMetaData.getPath() + ")");
		// System.out.println(getSpaces(level) + fileMetaData.getName());

		if (fileMetaData.isDirectory()) {
			int deeperLevel = level + 1;
			Path[] subPaths = fs.listFiles(path);
			for (Path subPath : subPaths) {
				walkFolders(fs, subPath, deeperLevel);
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
