package com.osgi.example1.fs.client.api.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.origin.common.rest.client.ClientException;

import com.osgi.example1.fs.client.api.FileRef;
import com.osgi.example1.fs.client.api.FileSystem;
import com.osgi.example1.fs.client.api.FileSystemConfiguration;
import com.osgi.example1.fs.client.ws.FileSystemClient;
import com.osgi.example1.fs.common.Path;

public class FileSystemImpl extends FileSystem {

	protected static final FileRef[] EMPTY_FILES = new FileRef[0];

	protected FileSystemConfiguration config;

	/**
	 * 
	 * @param config
	 */
	public FileSystemImpl(FileSystemConfiguration config) {
		this.config = config;
	}

	@Override
	public FileSystemConfiguration getConfiguration() {
		return this.config;
	}

	protected FileSystemClient getFsClient() {
		return this.config.getFileSystemClient();
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
	public FileRef[] listRoots() throws IOException {
		FileRef[] fileRefs = null;
		try {
			Path[] paths = getFsClient().listRoots();

			fileRefs = new FileRef[paths.length];
			for (int i = 0; i < paths.length; i++) {
				fileRefs[i] = FileRef.newInstance(this, paths[i].getPathString());
			}
		} catch (ClientException e) {
			handleClientException(e);
		}
		if (fileRefs == null) {
			fileRefs = EMPTY_FILES;
		}
		return fileRefs;
	}

	@Override
	public FileRef[] listFiles(FileRef parent) throws IOException {
		FileRef[] fileRefs = null;
		try {
			Path[] paths = getFsClient().listFiles(parent.path());

			fileRefs = new FileRef[paths.length];
			for (int i = 0; i < paths.length; i++) {
				fileRefs[i] = FileRef.newInstance(this, paths[i].getPathString());
			}
		} catch (ClientException e) {
			handleClientException(e);
		}
		if (fileRefs == null) {
			fileRefs = EMPTY_FILES;
		}
		return fileRefs;
	}

	@Override
	public boolean mkdirs(FileRef dir) throws IOException {
		try {
			return getFsClient().mkdirs(dir.path());
		} catch (ClientException e) {
			handleClientException(e);
		}
		return false;
	}

	@Override
	public boolean createNewFile(FileRef file) throws IOException {
		try {
			return getFsClient().createNewFile(file.path());
		} catch (ClientException e) {
			handleClientException(e);
		}
		return false;
	}

	@Override
	public boolean delete(FileRef file) throws IOException {
		try {
			return getFsClient().delete(file.path());
		} catch (ClientException e) {
			handleClientException(e);
		}
		return false;
	}

	@Override
	public InputStream getInputStream(Path path) throws IOException {
		return null;
	}

	// -----------------------------------------------------------------------------------------
	// Upload local file and directory to FS
	// -----------------------------------------------------------------------------------------
	@Override
	public boolean uploadFileToFile(File localFile, FileRef destFile) throws IOException {
		try {
			return getFsClient().uploadFileToFile(localFile, destFile.path());
		} catch (ClientException e) {
			handleClientException(e);
		}
		return false;
	}

	@Override
	public boolean uploadFileToDirectory(File localFile, FileRef destDir) throws IOException {
		try {
			return getFsClient().uploadFileToDirectory(localFile, destDir.path());
		} catch (ClientException e) {
			handleClientException(e);
		}
		return false;
	}

	@Override
	public boolean uploadDirectoryToDirectory(File localDir, FileRef destDir, boolean includingSourceDir) throws IOException {
		try {
			return getFsClient().uploadDirectoryToDirectory(localDir, destDir.path(), includingSourceDir);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return false;
	}

	// -----------------------------------------------------------------------------------------
	// Download file and directory from FS to local
	// -----------------------------------------------------------------------------------------
	@Override
	public File downloadFileToLocalFile(FileRef sourceFile, File localFile) throws IOException {
		return null;
	}

	@Override
	public File downloadFileToLocalDirectory(FileRef sourceFile, File localDir) throws IOException {
		return null;
	}

	@Override
	public boolean downloadDirectoryToLocalDirectory(FileRef sourceDir, File localDir, boolean includingSourceDir) throws IOException {
		return false;
	}

}
