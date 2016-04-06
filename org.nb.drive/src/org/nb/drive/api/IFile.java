package org.nb.drive.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IFile {

	public static final String PATH_SEPARATOR = "/";

	// -----------------------------------------------------------------
	// File attributes
	// -----------------------------------------------------------------
	/**
	 * Get parent file.
	 * 
	 * @return
	 */
	public IFile getParent();

	/**
	 * Get absolute path of the file.
	 * 
	 * @return
	 */
	public String getAbsolutePath();

	/**
	 * Get the path of the file.
	 * 
	 * @return
	 */
	public String getPath();

	/**
	 * Get File name (including file extension).
	 * 
	 * @return
	 */
	public String getFileName();

	/**
	 * Get file extension.
	 * 
	 * @return
	 */
	public String getFileExtension();

	/**
	 * Check whether the file exists.
	 * 
	 * @return
	 */
	public boolean exists();

	/**
	 * Check whether the file is a directory.
	 * 
	 * @return
	 */
	public boolean isDirectory();

	/**
	 * Check whether the file is a file.
	 * 
	 * @return
	 */
	public boolean isFile();

	/**
	 * Get the File size.
	 * 
	 * @return
	 */
	public long size();

	public long lastModified();

	// -----------------------------------------------------------------
	// File children
	// -----------------------------------------------------------------
	/**
	 * 
	 * @return
	 */
	public IFile[] listFiles();

	// -----------------------------------------------------------------
	// File operations
	// -----------------------------------------------------------------
	/**
	 * Create parent folders.
	 */
	public void mkdirs();

	/**
	 * Creates a new, empty file.
	 * 
	 * @return
	 */
	public boolean createNewFile();

	/**
	 * Delete the file.
	 * 
	 * @return
	 */
	public boolean delete();

	// -----------------------------------------------------------------
	// File input stream and output stream
	// -----------------------------------------------------------------
	/**
	 * Write the content of the File to a output stream.
	 * 
	 * @param output
	 */
	public void write(OutputStream output) throws IOException;

	/**
	 * Get the input stream of the file.
	 * 
	 * @return
	 */
	public InputStream read();

	/**
	 * Copy the content of a java.io.File into this file.
	 * 
	 * @param file
	 */
	public void copyFrom(File file);

	/**
	 * Copy the content of this file to a java.io.File.
	 * 
	 * @param file
	 */
	public void copyTo(File file);

}
