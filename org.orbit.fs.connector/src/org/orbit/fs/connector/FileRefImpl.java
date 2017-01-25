package org.orbit.fs.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.orbit.fs.api.FilePath;
import org.orbit.fs.api.FileRef;
import org.orbit.fs.api.FileSystem;
import org.orbit.fs.connector.ws.FileSystemWSClient;
import org.orbit.fs.model.FileMetadata;
import org.origin.common.rest.client.ClientException;

public class FileRefImpl extends FileRef {

	protected FileSystem fs;
	protected FilePath path;

	/**
	 * 
	 * @param fs
	 * @param fullPath
	 */
	public FileRefImpl(FileSystem fs, String fullPath) {
		this.fs = fs;
		this.path = new FilePath(fullPath);
	}

	/**
	 * 
	 * @param fs
	 * @param fullPath
	 */
	public FileRefImpl(FileSystem fs, FilePath fullPath) {
		this.fs = fs;
		this.path = fullPath;
	}

	/**
	 * 
	 * @param fs
	 * @param parent
	 * @param childPath
	 */
	public FileRefImpl(FileSystem fs, FileRef parent, String childPath) {
		this.fs = fs;
		this.path = new FilePath(parent.path(), childPath);
	}

	/**
	 * 
	 * @param fs
	 * @param parent
	 * @param childPath
	 */
	public FileRefImpl(FileSystem fs, FileRef parent, FilePath childPath) {
		this.fs = fs;
		this.path = new FilePath(parent.path(), childPath);
	}

	protected FileSystemWSClient getFsClient() {
		// return this.fs.getConfiguration().getFileSystemClient();
		return this.fs.getConfiguration().getAdapter(FileSystemWSClient.class);
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

	public FileSystem getFileSystem() {
		return this.fs;
	}

	@Override
	public FilePath path() {
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
		// return newInstance(this.fs, this.path.getParentPathString());
		return this.fs.getFile(this.path.getParentPathString());
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
		sb.append("FileRef [");
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
		sb.append("]");
		return sb.toString();
	}

}
