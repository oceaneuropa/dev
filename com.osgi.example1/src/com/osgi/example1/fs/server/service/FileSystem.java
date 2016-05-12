package com.osgi.example1.fs.server.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.origin.common.util.IAdaptable;

import com.osgi.example1.fs.common.Configuration;
import com.osgi.example1.fs.common.FileMetadata;
import com.osgi.example1.fs.common.Path;

public interface FileSystem extends IAdaptable {

	public void start();

	public void stop();

	/**
	 * Get configuration of the file system.
	 * 
	 * @return
	 */
	public Configuration getConfiguration();

	/**
	 * Get file metadata.
	 * 
	 * @param path
	 * @return
	 */
	public FileMetadata getFileMetaData(Path path);

	/**
	 * List files in root directory.
	 * 
	 * @return
	 */
	public Path[] listRoots();

	/**
	 * List files in a directory.
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
	 * Check whether a path is a directory.
	 * 
	 * @param path
	 * @return
	 */
	public boolean isDirectory(Path path);

	/**
	 * Create a directory.
	 * 
	 * @param path
	 * @return
	 */
	public boolean mkdirs(Path path) throws IOException;

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
	 * Copy the file content read from a input stream to a file in the FS.
	 * 
	 * @param inputStream
	 * @param destFilePath
	 * @return
	 * @throws IOException
	 */
	public Path copyInputStreamToFsFile(InputStream inputStream, Path destFilePath) throws IOException;

	/**
	 * Copy a local file to a file in the FS.
	 * 
	 * @param localFile
	 * @param destFilePath
	 * @return
	 * @throws IOException
	 */
	public Path copyFileToFsFile(File localFile, Path destFilePath) throws IOException;

	/**
	 * Copy a local file to a directory in the FS.
	 * 
	 * @param localFile
	 * @param destDirPath
	 * @return
	 * @throws IOException
	 */
	public Path copyFileToFsDirectory(File localFile, Path destDirPath) throws IOException;

	/**
	 * Copy a local directory to a directory in the FS.
	 * 
	 * @param localDir
	 * @param destDirPath
	 * @param includingSourceDir
	 * @return
	 * @throws IOException
	 */
	public boolean copyDirectoryToFsDirectory(File localDir, Path destDirPath, boolean includingSourceDir) throws IOException;

}
