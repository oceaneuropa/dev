package org.orbit.fs.connector.ws;

import java.io.IOException;

import org.orbit.fs.api.FilePath;
import org.orbit.fs.api.FileRef;
import org.orbit.fs.api.FileSystem;
import org.orbit.fs.model.FileMetadata;
import org.origin.common.rest.client.ClientException;

/**
 *
 */
public class FileSystemWSClientHelper {

	public static FileSystemWSClientHelper INSTANCE = new FileSystemWSClientHelper();

	/**
	 * Recursively walk through folders and print out folder path.
	 * 
	 * @param fs
	 * @param path
	 * @param level
	 * @throws ClientException
	 */
	public void walkFolders(FileSystemWSClient fsClient, FilePath path, int level) throws ClientException {
		FileMetadata fileMetaData = fsClient.getFileMetadata(path);
		System.out.println(getSpaces(level) + fileMetaData.getName() + " (" + fileMetaData.getPath() + ")");

		if (fileMetaData.isDirectory()) {
			int deeperLevel = level + 1;
			FilePath[] subPaths = fsClient.listFiles(path);
			for (FilePath subPath : subPaths) {
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
	public void walkFolders(FileSystem fs, FileRef file, int level) throws IOException {
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
