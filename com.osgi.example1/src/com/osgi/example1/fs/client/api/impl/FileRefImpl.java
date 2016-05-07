package com.osgi.example1.fs.client.api.impl;

import com.osgi.example1.fs.client.api.FileRef;

public class FileRefImpl implements FileRef {

	// path separator
	public static final String SEPARATOR = "/";
	public static final char SEPARATOR_CHAR = '/';

	protected String pathString;

	/**
	 * 
	 * @param pathString
	 */
	public FileRefImpl(String pathString) {
		this.pathString = pathString;
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public FileRefImpl(FileRef parent, String child) {
		this(parent.getPath(), child);
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public FileRefImpl(String parent, FileRef child) {
		this(parent, child.getPath());
	}

	/**
	 * 
	 * @param parentPath
	 * @param childPath
	 */
	public FileRefImpl(String parentPath, String childPath) {
		String path1 = parentPath;
		String path2 = childPath;

		String newPath = path1;

		// path1 ends with "/" and path2 starts with "/" --- make sure there is only one "/" between then.
		if (path1.endsWith(SEPARATOR) && path2.startsWith(SEPARATOR)) {
			if (path2.length() > 1) {
				path2 = path2.substring(1);
			} else {
				// path2's length is either 1 or greater than 1. if not greater than 1, must be 1 --- the "/"
				path2 = "";
			}
		}
		if (!path1.endsWith(SEPARATOR) && !path2.startsWith(SEPARATOR)) {
			newPath += SEPARATOR;
		}

		newPath += path2;

		this.pathString = newPath;
	}

	protected void checkUpdate() {

	}

	@Override
	public String getPath() {
		return this.pathString;
	}

	@Override
	public String getParent() {
		return null;
	}

	@Override
	public FileRef getParentFile() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public boolean isHidden() {
		return false;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public boolean canExecute() {
		return false;
	}

	@Override
	public boolean canRead() {
		return false;
	}

	@Override
	public boolean canWrite() {
		return false;
	}

	@Override
	public long getLength() {
		return 0;
	}

	@Override
	public long getLastModified() {
		return 0;
	}

}
