package com.osgi.example1.fs.client.api.impl;

import java.io.IOException;

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

	protected FileSystemClient getFsClient() {
		return this.fs.getConfiguration().getFileSystemClient();
	}

	/**
	 * Throw IOException with ClientException.
	 * 
	 * @param e
	 * @throws IOException
	 */
	protected void handleClientException(ClientException e) throws IOException {
		e.printStackTrace();

		int code = e.getCode();
		String message = e.getMessage();
		Throwable t = e.getCause();

		String newMessage = message + " (" + code + ")";
		if (t != null) {
			newMessage += " (" + t.getClass().getSimpleName() + ":" + t.getMessage() + ")";
		}
		throw new IOException(newMessage);
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
	public String getName() throws IOException {
		String name = null;
		try {
			name = getFsClient().getFileAttribute(this.path, FileMetadata.NAME, String.class);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return name;
	}

	@Override
	public boolean isDirectory() throws IOException {
		boolean isDirectory = false;
		try {
			isDirectory = getFsClient().getFileAttribute(this.path, FileMetadata.IS_DIRECTORY, Boolean.class);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return isDirectory;
	}

	@Override
	public boolean isHidden() throws IOException {
		boolean isHidden = false;
		try {
			isHidden = getFsClient().getFileAttribute(this.path, FileMetadata.IS_HIDDEN, Boolean.class);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return isHidden;
	}

	@Override
	public boolean exists() throws IOException {
		boolean exists = false;
		try {
			exists = getFsClient().getFileAttribute(this.path, FileMetadata.EXISTS, Boolean.class);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return exists;
	}

	@Override
	public boolean canExecute() throws IOException {
		boolean canExecute = false;
		try {
			canExecute = getFsClient().getFileAttribute(this.path, FileMetadata.CAN_EXECUTE, Boolean.class);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return canExecute;
	}

	@Override
	public boolean canRead() throws IOException {
		boolean canRead = false;
		try {
			canRead = getFsClient().getFileAttribute(this.path, FileMetadata.CAN_READ, Boolean.class);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return canRead;
	}

	@Override
	public boolean canWrite() throws IOException {
		boolean canWrite = false;
		try {
			canWrite = getFsClient().getFileAttribute(this.path, FileMetadata.CAN_WRITE, Boolean.class);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return canWrite;
	}

	@Override
	public long getLength() throws IOException {
		long length = 0;
		try {
			length = getFsClient().getFileAttribute(this.path, FileMetadata.LENGTH, Long.class);
			// Object attrValue = getClient().getFileAttribute(this.path, FileMetadata.LENGTH, Long.class);
			// if (attrValue instanceof Long) {
			// length = (long) attrValue;
			// } else if (attrValue instanceof Integer) {
			// length = Long.valueOf((Integer) attrValue);
			// } else if (attrValue instanceof String) {
			// length = Long.valueOf((String) attrValue);
			// }
		} catch (ClientException e) {
			handleClientException(e);
		}
		return length;
	}

	@Override
	public long getLastModified() throws IOException {
		long lastModified = 0;
		try {
			lastModified = getFsClient().getFileAttribute(this.path, FileMetadata.LAST_MODIFIED, Long.class);
			// Object attrValue = getClient().getFileAttribute(this.path, FileMetadata.LAST_MODIFIED, Long.class);
			// if (attrValue instanceof Long) {
			// lastModified = (long) attrValue;
			// } else if (attrValue instanceof Integer) {
			// lastModified = Long.valueOf((Integer) attrValue);
			// } else if (attrValue instanceof String) {
			// lastModified = Long.valueOf((String) attrValue);
			// }
		} catch (ClientException e) {
			handleClientException(e);
		}
		return lastModified;
	}

	@Override
	public boolean createNewFile() throws IOException {
		return this.fs.createNewFile(this);
	}

	@Override
	public boolean mkdirs() throws IOException {
		return this.fs.mkdirs(this);
	}

	@Override
	public boolean delete() throws IOException {
		return this.fs.delete(this);
	}

}
