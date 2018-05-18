package org.orbit.fs.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.orbit.fs.model.FileMetadata;
import org.origin.common.adapter.IAdaptable;
import org.origin.common.resource.IPath;

public interface FileSystem extends IAdaptable {

	public void start();

	public void stop();

	/**
	 * Get configuration of the file system.
	 * 
	 * @return
	 */
	public FileSystemConfiguration getConfig();

	/**
	 * Set configuration of the file system.
	 * 
	 * @param config
	 */
	public void setConfig(FileSystemConfiguration config);

	/**
	 * Get file metadata.
	 * 
	 * @param path
	 * @return
	 */
	public FileMetadata getFileMetaData(IPath path);

	/**
	 * List files in root directory.
	 * 
	 * @return
	 */
	public IPath[] listRoots();

	/**
	 * List files in a directory.
	 * 
	 * @param parent
	 * @return
	 */
	public IPath[] listFiles(IPath parent);

	/**
	 * Check whether a file or a directory exists.
	 * 
	 * @param path
	 * @return
	 */
	public boolean exists(IPath path);

	/**
	 * Check whether a path is a directory.
	 * 
	 * @param path
	 * @return
	 */
	public boolean isDirectory(IPath path);

	/**
	 * Create a directory.
	 * 
	 * @param path
	 * @return
	 */
	public boolean mkdirs(IPath path) throws IOException;

	/**
	 * Create a new, empty file if and only if a file with this name does not yet exist.
	 *
	 * @param path
	 * @return true if the named file does not exist and was successfully created; false if the named file already exists.
	 * @throws IOException
	 */
	public boolean createNewFile(IPath path) throws IOException;

	/**
	 * Delete a file or a directory.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public boolean delete(IPath path) throws IOException;

	/**
	 * Get the input stream of a file.
	 * 
	 * @param path
	 * @return InputStream of the file. The client which calls this method is responsible for closing the InputStream object.
	 * @throws IOException
	 */
	public InputStream getInputStream(IPath path) throws IOException;

	/**
	 * Copy the file content read from a input stream to a file in the FS.
	 * 
	 * @param inputStream
	 * @param destFilePath
	 * @return
	 * @throws IOException
	 */
	public IPath copyInputStreamToFsFile(InputStream inputStream, IPath destFilePath) throws IOException;

	/**
	 * Copy a local file to a file in the FS.
	 * 
	 * @param localFile
	 * @param destFilePath
	 * @return
	 * @throws IOException
	 */
	public IPath copyFileToFsFile(File localFile, IPath destFilePath) throws IOException;

	/**
	 * Copy a local file to a directory in the FS.
	 * 
	 * @param localFile
	 * @param destDirPath
	 * @return
	 * @throws IOException
	 */
	public IPath copyFileToFsDirectory(File localFile, IPath destDirPath) throws IOException;

	/**
	 * Copy a local directory to a directory in the FS.
	 * 
	 * @param localDir
	 * @param destDirPath
	 * @param includingSourceDir
	 * @return
	 * @throws IOException
	 */
	public boolean copyDirectoryToFsDirectory(File localDir, IPath destDirPath, boolean includingSourceDir) throws IOException;

}
