package com.osgi.example1.fs.client.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.osgi.example1.fs.client.api.impl.FileRefImpl;
import com.osgi.example1.fs.client.api.impl.FileSystemImpl;
import com.osgi.example1.fs.common.Path;

/**
 * http://stackoverflow.com/questions/12113343/reading-directly-from-google-drive-in-java
 * 
 * Please I need to read the content of a file stored in Google Drive programmatically. I'm looking forward to some sort of InputStream is =
 * <drive_stuff>.read(fileID); Any help? I'll also appreciate if I can write back to a file using some sort of
 * 
 * OutputStream dos = new DriveOutputStream(driveFileID); dos.write(data);
 * 
 * If this sort of convenient approach is too much for what Drive can offer, please I'll like to have suggestions on how I can read/write to Drive
 * directly from java.io.InputStream / OutputStream / Reader / Writer without creating temporary local file copies of the data I want to ship to
 * drive.
 */
public abstract class FileSystem {

	public static FileSystem create(FileSystemConfiguration config) {
		return new FileSystemImpl(config);
	}

	public abstract FileSystemConfiguration getConfiguration();

	private FileRef ROOT = null;

	public synchronized FileRef root() {
		if (ROOT == null) {
			ROOT = new FileRefImpl(this, Path.ROOT.getPathString());
		}
		return ROOT;
	}

	/**
	 * List all file in the root directory.
	 * 
	 * @return
	 */
	public abstract FileRef[] listRootFiles();

	/**
	 * List all files in a directory.
	 * 
	 * @param parent
	 * @return
	 */
	public abstract FileRef[] listFiles(FileRef parent);

	/**
	 * Create a directory.
	 * 
	 * @param dir
	 * @return
	 */
	public abstract boolean mkdirs(FileRef dir);

	/**
	 * Create a new, empty file if and only if a file with this name does not yet exist.
	 *
	 * @param file
	 * @return true if the named file does not exist and was successfully created; false if the named file already exists.
	 * @throws IOException
	 */
	public abstract boolean createNewFile(FileRef file) throws IOException;

	/**
	 * Delete a file or a directory.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public abstract boolean delete(FileRef file) throws IOException;

	/**
	 * Get the input stream of a file.
	 * 
	 * @param path
	 * @return InputStream of the file. The client which calls this method is responsible for closing the InputStream object.
	 * @throws IOException
	 */
	public abstract InputStream getInputStream(Path path) throws IOException;

	// -----------------------------------------------------------------------------------------
	// Upload local file and directory to FS
	// -----------------------------------------------------------------------------------------
	/**
	 * Upload a local file to a file in the FS.
	 * 
	 * @param localFile
	 * @param destFile
	 * @return
	 * @throws IOException
	 */
	public abstract FileRef uploadLocalFileToFile(File localFile, FileRef destFile) throws IOException;

	/**
	 * Upload a local file to a directory in the FS.
	 * 
	 * @param localFile
	 * @param destDir
	 * @return
	 * @throws IOException
	 */
	public abstract FileRef uploadLocalFileToDirectory(File localFile, FileRef destDir) throws IOException;

	/**
	 * Upload a local directory to a directory in the FS.
	 * 
	 * @param localDir
	 * @param destDir
	 * @param includingSourceDir
	 * @return
	 * @throws IOException
	 */
	public abstract boolean uploadLocalDirectoryToDirectory(File localDir, FileRef destDir, boolean includingSourceDir) throws IOException;

	// -----------------------------------------------------------------------------------------
	// Download file and directory from FS to local
	// -----------------------------------------------------------------------------------------
	/**
	 * Download a file from the FS to a local file.
	 * 
	 * @param sourceFile
	 * @param localFile
	 * @return
	 * @throws IOException
	 */
	public abstract File downloadFileToLocalFile(FileRef sourceFile, File localFile) throws IOException;

	/**
	 * Download a file from the FS to a local directory.
	 * 
	 * @param sourceFile
	 * @param localDir
	 * @return
	 * @throws IOException
	 */
	public abstract File downloadFileToLocalDirectory(FileRef sourceFile, File localDir) throws IOException;

	/**
	 * Download a directory from the FS to a local directory.
	 * 
	 * @param sourceDir
	 * @param localDir
	 * @param includingSourceDir
	 * @return
	 * @throws IOException
	 */
	public abstract boolean downloadDirectoryToLocalDirectory(FileRef sourceDir, File localDir, boolean includingSourceDir) throws IOException;

}
