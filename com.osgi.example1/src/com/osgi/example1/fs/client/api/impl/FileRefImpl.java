package com.osgi.example1.fs.client.api.impl;

import org.origin.common.rest.client.ClientException;

import com.osgi.example1.fs.client.api.FileRef;
import com.osgi.example1.fs.client.api.FileSystem;
import com.osgi.example1.fs.client.ws.FileSystemClient;
import com.osgi.example1.fs.common.FileMetadata;
import com.osgi.example1.fs.common.Path;

public class FileRefImpl extends FileRef {

	protected FileSystem fs;
	protected Path path;

	/**
	 * 
	 * @param fs
	 * @param pathString
	 */
	public FileRefImpl(FileSystem fs, String pathString) {
		this.fs = fs;
		this.path = new Path(pathString);
	}

	/**
	 * 
	 * @param fs
	 * @param parent
	 * @param child
	 */
	public FileRefImpl(FileSystem fs, FileRef parent, String child) {
		this.fs = fs;
		this.path = new Path(parent.path(), child);
	}

	/**
	 * 
	 * @param fs
	 * @param parent
	 * @param child
	 */
	public FileRefImpl(FileSystem fs, String parent, FileRef child) {
		this.fs = fs;
		this.path = new Path(parent, child.path());
	}

	/**
	 * 
	 * @param fs
	 * @param parentPath
	 * @param childPath
	 */
	public FileRefImpl(FileSystem fs, String parentPath, String childPath) {
		this.fs = fs;
		this.path = new Path(parentPath, childPath);
	}

	public FileSystem getFileSystem() {
		return this.fs;
	}

	protected FileSystemClient getClient() {
		return this.fs.getConfiguration().getFileSystemClient();
	}

	@Override
	public Path path() {
		return this.path;
	}

	@Override
	public String getPath() {
		return this.path.getPathString();
	}

	@Override
	public String getParent() {
		return this.path.getParentPathString();
	}

	@Override
	public FileRef getParentFile() {
		return FileRef.newInstance(this.fs, this.path.getParentPathString());
	}

	@Override
	public String getName() {
		String name = null;
		try {
			name = getClient().getFileAttribute(this.path, FileMetadata.NAME, String.class);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return name;
	}

	@Override
	public boolean isDirectory() {
		boolean isDirectory = false;
		try {
			isDirectory = getClient().getFileAttribute(this.path, FileMetadata.IS_DIRECTORY, Boolean.class);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return isDirectory;
	}

	@Override
	public boolean isHidden() {
		boolean isHidden = false;
		try {
			isHidden = getClient().getFileAttribute(this.path, FileMetadata.IS_HIDDEN, Boolean.class);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return isHidden;
	}

	@Override
	public boolean exists() {
		boolean exists = false;
		try {
			exists = getClient().getFileAttribute(this.path, FileMetadata.EXISTS, Boolean.class);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return exists;
	}

	@Override
	public boolean canExecute() {
		boolean canExecute = false;
		try {
			canExecute = getClient().getFileAttribute(this.path, FileMetadata.CAN_EXECUTE, Boolean.class);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return canExecute;
	}

	@Override
	public boolean canRead() {
		boolean canRead = false;
		try {
			canRead = getClient().getFileAttribute(this.path, FileMetadata.CAN_READ, Boolean.class);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return canRead;
	}

	@Override
	public boolean canWrite() {
		boolean canWrite = false;
		try {
			canWrite = getClient().getFileAttribute(this.path, FileMetadata.CAN_WRITE, Boolean.class);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return canWrite;
	}

	@Override
	public long getLength() {
		long length = 0;
		try {
			length = getClient().getFileAttribute(this.path, FileMetadata.LENGTH, Long.class);
			// Object attrValue = getClient().getFileAttribute(this.path, FileMetadata.LENGTH, Long.class);
			// if (attrValue instanceof Long) {
			// length = (long) attrValue;
			// } else if (attrValue instanceof Integer) {
			// length = Long.valueOf((Integer) attrValue);
			// } else if (attrValue instanceof String) {
			// length = Long.valueOf((String) attrValue);
			// }
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return length;
	}

	@Override
	public long getLastModified() {
		long lastModified = 0;
		try {
			lastModified = getClient().getFileAttribute(this.path, FileMetadata.LAST_MODIFIED, Long.class);
			// Object attrValue = getClient().getFileAttribute(this.path, FileMetadata.LAST_MODIFIED, Long.class);
			// if (attrValue instanceof Long) {
			// lastModified = (long) attrValue;
			// } else if (attrValue instanceof Integer) {
			// lastModified = Long.valueOf((Integer) attrValue);
			// } else if (attrValue instanceof String) {
			// lastModified = Long.valueOf((String) attrValue);
			// }
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return lastModified;
	}

}
