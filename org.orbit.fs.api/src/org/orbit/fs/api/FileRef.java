package org.orbit.fs.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public abstract class FileRef {

	/**
	 * List root files.
	 * 
	 * @param fs
	 * @return
	 * @throws IOException
	 */
	public static FileRef[] listRoots(FileSystem fs) throws IOException {
		return fs.listRoots();
	}

	/**
	 * List files in a parent ref file.
	 * 
	 * @param parent
	 * @return
	 * @throws IOException
	 */
	public static FileRef[] listFiles(FileRef parent) throws IOException {
		FileSystem fs = parent.getFileSystem();
		return fs.listFiles(parent);
	}

	/**
	 * Get the file system.
	 * 
	 * @return
	 */
	public abstract FileSystem getFileSystem();

	/**
	 * Get the reference path of the file.
	 * 
	 * @return
	 */
	public abstract FilePath path();

	/**
	 * Get ref file path string.
	 * 
	 * @return
	 */
	public abstract String getPath();

	/**
	 * Get parent ref file path string.
	 * 
	 * @return
	 */
	public abstract String getParent();

	/**
	 * Get parent ref file.
	 * 
	 * @return
	 */
	public abstract FileRef getParentFile();

	/**
	 * Get ref file name.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract String getName() throws IOException;

	/**
	 * Check whether the ref file is a directory.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract boolean isDirectory() throws IOException;

	/**
	 * Check whether the ref file is hidden.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract boolean isHidden() throws IOException;

	/**
	 * Check whether the ref file exists.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract boolean exists() throws IOException;

	/**
	 * Check whether the ref file can be executed.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract boolean canExecute() throws IOException;

	/**
	 * Check whether the ref file can be read.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract boolean canRead() throws IOException;

	/**
	 * Check whether the ref file can be written.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract boolean canWrite() throws IOException;

	/**
	 * Get the length of the ref file.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract long getLength() throws IOException;

	/**
	 * Get the last modified time of the ref file.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract long getLastModified() throws IOException;

	/**
	 * Create new empty ref file.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract boolean createNewFile() throws IOException;

	/**
	 * Create directories for a ref directory.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract boolean mkdirs() throws IOException;

	/**
	 * Delete a ref file or a ref directory.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract boolean delete() throws IOException;

	/**
	 * Get the InputStream of a ref file.
	 * 
	 * @param fileRef
	 * @return InputStream of the file. The client which calls this method is responsible for closing the InputStream.
	 * @throws IOException
	 */
	public abstract InputStream getInputStream(FileRef fileRef) throws IOException;

	/**
	 * Get the OutputStream of a ref file.
	 * 
	 * @param fileRef
	 * @return OutputStream of the file. The client which calls this method is responsible for closing the OutputStream.
	 * @throws IOException
	 */
	public abstract OutputStream getOutputStream(FileRef fileRef) throws IOException;

}
