package com.osgi.example1.fs.client.api.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.origin.common.rest.client.ClientException;

import com.osgi.example1.fs.client.api.FileRef;
import com.osgi.example1.fs.client.api.FileSystem;
import com.osgi.example1.fs.client.ws.FileSystemWSClient;
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

	protected FileSystemWSClient getFsClient() {
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
		if (this.path.isRoot()) {
			return this.path.getPathString();
		}
		if (this.path.isEmpty()) {
			return null;
		}
		String name = this.path.getLastSegment();
		return name;
	}

	@Override
	public boolean isDirectory() throws IOException {
		boolean isDirectory = false;
		try {
			isDirectory = getFsClient().isDirectory(this.path);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return isDirectory;
	}

	@Override
	public boolean isHidden() throws IOException {
		boolean isHidden = false;
		try {
			isHidden = getFsClient().isHidden(this.path);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return isHidden;
	}

	@Override
	public boolean exists() throws IOException {
		boolean exists = false;
		try {
			exists = getFsClient().exists(this.path);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return exists;
	}

	@Override
	public boolean canExecute() throws IOException {
		boolean canExecute = false;
		try {
			canExecute = getFsClient().canExecute(this.path);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return canExecute;
	}

	@Override
	public boolean canRead() throws IOException {
		boolean canRead = false;
		try {
			canRead = getFsClient().canRead(this.path);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return canRead;
	}

	@Override
	public boolean canWrite() throws IOException {
		boolean canWrite = false;
		try {
			canWrite = getFsClient().canWrite(this.path);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return canWrite;
	}

	@Override
	public long getLength() throws IOException {
		long length = 0;
		try {
			length = getFsClient().getLength(this.path);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return length;
	}

	@Override
	public long getLastModified() throws IOException {
		long lastModified = 0;
		try {
			lastModified = getFsClient().getLastModified(this.path);
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

	@Override
	public InputStream getInputStream(FileRef fileRef) throws IOException {
		return this.fs.getInputStream(this);
	}

	@Override
	public OutputStream getOutputStream(FileRef fileRef) throws IOException {
		return this.fs.getOutputStream(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FileRef (");
		try {
			FileMetadata metadata = getFsClient().getFileMetadata(this.path);
			if (metadata != null) {
				sb.append("name='").append(metadata.getName()).append("'");
				sb.append(", isDirectory=").append(metadata.isDirectory());
				sb.append(", isHidden=").append(metadata.isHidden());
				sb.append(", path='").append(metadata.getPath()).append("'");
				sb.append(", parentPath='").append(metadata.getParentPath()).append("'");
				sb.append(", exists=").append(metadata.exists());
				sb.append(", canExecute=").append(metadata.canExecute());
				sb.append(", canRead=").append(metadata.canRead());
				sb.append(", canWrite=").append(metadata.canWrite());
				sb.append(", length=").append(metadata.getLength());
				sb.append(", lastModified=").append(metadata.getLastModified());
			} else {
				sb.append("path='").append(this.path).append("'");
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		sb.append(")");
		return sb.toString();
	}

}
