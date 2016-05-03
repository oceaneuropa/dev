package com.osgi.example1.fs.server.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.osgi.example1.fs.common.Configuration;
import com.osgi.example1.fs.common.Path;
import com.osgi.example1.fs.common.dto.FileMetaData;

public interface FileSystem {

	/**
	 * Get configuration of the file system.
	 * 
	 * @return
	 */
	public Configuration getConfiguration();

	/**
	 * Get file meta data.
	 * 
	 * @param path
	 * @return
	 */
	public FileMetaData getFileMetaData(Path path);

	/**
	 * List all file in the root directory.
	 * 
	 * @return
	 */
	public Path[] listRootFiles();

	/**
	 * List all files in a directory.
	 * 
	 * @param parent
	 * @return
	 */
	public Path[] listFiles(Path parent);

	/**
	 * Check whether a file or a directory exists.
	 * 
	 * @param path
	 * @return
	 */
	public boolean exists(Path path);

	/**
	 * Create a directory.
	 * 
	 * @param path
	 * @return
	 */
	public boolean mkdirs(Path path);

	/**
	 * Create a new, empty file if and only if a file with this name does not yet exist.
	 *
	 * @param path
	 * @return true if the named file does not exist and was successfully created; false if the named file already exists.
	 * @throws IOException
	 */
	public boolean createNewFile(Path path) throws IOException;

	/**
	 * Delete a file or a directory.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public boolean delete(Path path) throws IOException;

	/**
	 * Get the input stream of a file.
	 * 
	 * @param path
	 * @return InputStream of the file. The client which calls this method is responsible for closing the InputStream object.
	 * @throws IOException
	 */
	public InputStream getInputStream(Path path) throws IOException;

	/**
	 * Copy a local file to a file in the FS.
	 * 
	 * @param localFile
	 * @param destFilePath
	 * @return
	 * @throws IOException
	 */
	public Path copyLocalFileToFile(File localFile, Path destFilePath) throws IOException;

	/**
	 * Copy a local file to a directory in the FS.
	 * 
	 * @param localFile
	 * @param destDirPath
	 * @return
	 * @throws IOException
	 */
	public Path copyLocalFileToDirectory(File localFile, Path destDirPath) throws IOException;

	/**
	 * Copy a local directory to a directory in the FS.
	 * 
	 * @param localDir
	 * @param destDirPath
	 * @param includingSourceDir
	 * @return
	 * @throws IOException
	 */
	public boolean copyLocalDirectoryToDirectory(File localDir, Path destDirPath, boolean includingSourceDir) throws IOException;

}
