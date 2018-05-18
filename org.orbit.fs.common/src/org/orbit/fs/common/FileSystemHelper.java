package org.orbit.fs.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.orbit.fs.api.FilePath;
import org.orbit.fs.model.FileMetadata;
import org.origin.common.io.FileUtil;
import org.origin.common.io.IOUtil;
import org.origin.common.resource.IPath;

public class FileSystemHelper {

	public static FileSystemHelper INSTANCE = new FileSystemHelper();

	/**
	 * Recursively copy a fs file to a local directory.
	 * 
	 * @param fs
	 * @param sourceDirPath
	 * @param localDir
	 * @param includingSourceDir
	 * @return
	 * @throws IOException
	 */
	public boolean copyFsDirectoryToLocalDirectory(FileSystem fs, FilePath sourceDirPath, File localDir, boolean includingSourceDir) throws IOException {
		FileMetadata metadata = fs.getFileMetaData(sourceDirPath);
		if (metadata == null || !metadata.exists()) {
			System.err.println("Source '" + sourceDirPath.getPathString() + "' does not exist.");
			return false;
		}
		if (!metadata.isDirectory()) {
			System.err.println("Source '" + sourceDirPath.getPathString() + "' exists but is not a directory.");
			return false;
		}
		if (includingSourceDir) {
			copyFsFileToLocalDirectory(fs, sourceDirPath, localDir);
		} else {
			IPath[] memberPaths = fs.listFiles(sourceDirPath);
			for (IPath memberPath : memberPaths) {
				copyFsFileToLocalDirectory(fs, memberPath, localDir);
			}
		}
		return true;
	}

	/**
	 * Recursively copy a fs file or fs directory to a local directory.
	 * 
	 * @param fs
	 * @param sourcePath
	 * @param localDir
	 * @throws IOException
	 */
	public void copyFsFileToLocalDirectory(FileSystem fs, IPath sourcePath, File localDir) throws IOException {
		FileMetadata metadata = fs.getFileMetaData(sourcePath);
		if (metadata == null || !metadata.exists()) {
			System.err.println("Source '" + sourcePath.getPathString() + "' does not exist.");
			return;
		}

		// System.out.println(sourcePath.getPathString() + " -> " + localDir.getAbsolutePath());
		if (metadata.isDirectory()) {
			// sourcePath is directory
			String dirName = sourcePath.getLastSegment();

			// create new local dir
			File newLocalDir = new File(localDir, dirName);
			newLocalDir.mkdirs();

			// recursively copy sourcePath's member paths to the new local dir.
			IPath[] memberPaths = fs.listFiles(sourcePath);
			for (IPath memberPath : memberPaths) {
				copyFsFileToLocalDirectory(fs, memberPath, newLocalDir);
			}

		} else {
			// sourcePath is file
			String fileName = sourcePath.getLastSegment();
			File newLocalFile = new File(localDir, fileName);

			if (metadata.getLength() > 0) {
				// if fs file has content, copy it to local dir
				InputStream is = null;
				try {
					is = fs.getInputStream(sourcePath);
					FileUtil.copyInputStreamToFile(is, newLocalFile);
				} finally {
					IOUtil.closeQuietly(is, true);
				}
			} else {
				// if fs file doesn't have content, create new local file if not
				if (!newLocalFile.exists()) {
					newLocalFile.createNewFile();
				}
			}
		}
	}

	/**
	 * Recursively walk through folders and print out folder path.
	 * 
	 * @param fs
	 * @param path
	 * @param level
	 */
	public void walkFolders(FileSystem fs, IPath path, int level) {
		FileMetadata fileMetaData = fs.getFileMetaData(path);
		// System.out.println(getSpaces(level) + "path= " + path.getName());
		// System.out.println(getSpaces(level) + "fileMetaData = " + fileMetaData.toString());
		// System.out.println(getSpaces(level) + path.getName() + " " + fileMetaData.toString());
		// System.out.println(getSpaces(level) + fileMetaData.getName());
		System.out.println(getSpaces(level) + fileMetaData.getName() + " (" + fileMetaData.getPath() + ")");

		if (fileMetaData.isDirectory()) {
			int deeperLevel = level + 1;
			IPath[] subPaths = fs.listFiles(path);
			for (IPath subPath : subPaths) {
				walkFolders(fs, subPath, deeperLevel);
			}
		}
	}

	/**
	 * 
	 * @param level
	 * @return
	 */
	protected String getSpaces(int level) {
		String spaces = ""; // $NON-NLS-1$
		for (int i = 0; i < level; i++) {
			spaces += "    "; // $NON-NLS-1$
		}
		return spaces;
	}

}
