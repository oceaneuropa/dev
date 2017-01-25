package org.orbit.fs.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * http://stackoverflow.com/questions/12113343/reading-directly-from-google-drive-in-java
 * 
 * Please I need to read the content of a file stored in Google Drive programmatically. I'm looking forward to some sort of InputStream is = <drive_stuff>.read(fileID); Any help? I'll also appreciate
 * if I can write back to a file using some sort of
 * 
 * OutputStream dos = new DriveOutputStream(driveFileID);
 * 
 * dos.write(data);
 * 
 * If this sort of convenient approach is too much for what Drive can offer, please I'll like to have suggestions on how I can read/write to Drive directly from java.io.InputStream / OutputStream /
 * Reader / Writer without creating temporary local file copies of the data I want to ship to drive.
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface FileSystem {

	FileSystemConfiguration getConfiguration();

	/**
	 * 
	 * @param fullPath
	 * @return
	 */
	FileRef getFile(String fullPath);

	/**
	 * 
	 * @param fullPath
	 * @return
	 */
	FileRef getFile(FilePath fullPath);

	/**
	 * 
	 * @param parent
	 * @param childPath
	 * @return
	 */
	FileRef getFile(FileRef parent, String childPath);

	/**
	 * 
	 * @param parent
	 * @param childPath
	 * @return
	 */
	FileRef getFile(FileRef parent, FilePath childPath);

	/**
	 * 
	 * @return
	 */
	FileRef root();

	/**
	 * List all file in the root directory.
	 * 
	 * @return
	 * @throws IOException
	 */
	FileRef[] listRoots() throws IOException;

	/**
	 * List all files in a directory.
	 * 
	 * @param parent
	 * @return
	 * @throws IOException
	 */
	FileRef[] listFiles(FileRef parent) throws IOException;

	/**
	 * Create a directory.
	 * 
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	boolean mkdirs(FileRef dir) throws IOException;

	/**
	 * Create a new, empty file if and only if a file with this name does not yet exist.
	 *
	 * @param file
	 * @return true if the named file does not exist and was successfully created; false if the named file already exists.
	 * @throws IOException
	 */
	boolean createNewFile(FileRef file) throws IOException;

	/**
	 * Delete a file or a directory.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	boolean delete(FileRef file) throws IOException;

	/**
	 * Get the InputStream of a FileRef.
	 * 
	 * @param fileRef
	 * @return InputStream of the file. The client which calls this method is responsible for closing the InputStream.
	 * @throws IOException
	 */
	InputStream getInputStream(FileRef fileRef) throws IOException;

	/**
	 * Get the OutputStream of a FileRef.
	 * 
	 * @param fileRef
	 * @return OutputStream of the file. The client which calls this method is responsible for closing the OutputStream.
	 * @throws IOException
	 */
	OutputStream getOutputStream(FileRef fileRef) throws IOException;

	/**
	 * Upload a InputStream to a file in the FS.
	 * 
	 * @param input
	 * @param destFile
	 * @return
	 * @throws IOException
	 */
	boolean uploadInputStreamToFsFile(InputStream input, FileRef destFile) throws IOException;

	/**
	 * Upload a local file to a file in the FS.
	 * 
	 * @param localFile
	 * @param destFile
	 * @return
	 * @throws IOException
	 */
	boolean uploadFileToFsFile(File localFile, FileRef destFile) throws IOException;

	/**
	 * Upload a local file to a directory in the FS.
	 * 
	 * @param localFile
	 * @param destDir
	 * @return
	 * @throws IOException
	 */
	boolean uploadFileToFsDirectory(File localFile, FileRef destDir) throws IOException;

	/**
	 * Upload a local directory to a directory in the FS.
	 * 
	 * @param localDir
	 * @param destDir
	 * @param includingSourceDir
	 * @return
	 * @throws IOException
	 */
	boolean uploadDirectoryToFsDirectory(File localDir, FileRef destDir, boolean includingSourceDir) throws IOException;

	/**
	 * Download a file from FS and write the file content to a OutputStream.
	 * 
	 * @param sourceFileRef
	 * @param output
	 * @return
	 * @throws IOException
	 */
	boolean downloadFsFileToOutputStream(FileRef sourceFileRef, OutputStream output) throws IOException;

	/**
	 * Download a file from the FS to a local file.
	 * 
	 * @param sourceFileRef
	 * @param localFile
	 * @return
	 * @throws IOException
	 */
	boolean downloadFsFileToFile(FileRef sourceFileRef, File localFile) throws IOException;

	/**
	 * Download a file from the FS to a local directory.
	 * 
	 * @param sourceFileRef
	 * @param localDir
	 * @return
	 * @throws IOException
	 */
	boolean downloadFsFileToDirectory(FileRef sourceFileRef, File localDir) throws IOException;

	/**
	 * Download a directory from the FS to a local directory.
	 * 
	 * @param sourceDirRef
	 * @param localDir
	 * @param includingSourceDir
	 * @return
	 * @throws IOException
	 */
	boolean downloadFsDirectoryToDirectory(FileRef sourceDirRef, File localDir, boolean includingSourceDir) throws IOException;

}
