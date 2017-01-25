package org.orbit.fs.connector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.orbit.fs.api.FilePath;
import org.orbit.fs.api.FileRef;
import org.orbit.fs.api.FileSystem;
import org.orbit.fs.api.FileSystemConfiguration;
import org.orbit.fs.connector.ws.FileSystemWSClient;
import org.origin.common.rest.client.ClientException;

public class FileSystemImpl implements FileSystem {

	protected static final FileRef[] EMPTY_FILES = new FileRef[0];

	/**
	 * 
	 * @param config
	 * @return
	 */
	public static FileSystem newInstance(FileSystemConfiguration config) {
		return new FileSystemImpl(config);
	}

	protected FileSystemConfiguration config;
	protected FileRef root = null;

	/**
	 * 
	 * @param config
	 */
	public FileSystemImpl(FileSystemConfiguration config) {
		this.config = config;
	}

	@Override
	public FileRef getFile(String fullPath) {
		FileRef fileRef = new FileRefImpl(this, fullPath);
		return fileRef;
	}

	@Override
	public FileRef getFile(FilePath fullPath) {
		FileRef fileRef = new FileRefImpl(this, fullPath);
		return fileRef;
	}

	@Override
	public FileRef getFile(FileRef parent, String childPath) {
		FileRef fileRef = new FileRefImpl(parent.getFileSystem(), parent, childPath);
		return fileRef;
	}

	@Override
	public FileRef getFile(FileRef parent, FilePath childPath) {
		FileRef fileRef = new FileRefImpl(parent.getFileSystem(), parent, childPath);
		return fileRef;
	}

	public synchronized FileRef root() {
		if (root == null) {
			root = new FileRefImpl(this, FilePath.ROOT.getPathString());
		}
		return root;
	}

	@Override
	public FileSystemConfiguration getConfiguration() {
		return this.config;
	}

	protected FileSystemWSClient getFsClient() {
		// return this.config.getFileSystemClient();
		return this.config.getAdapter(FileSystemWSClient.class);
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
			FilePath[] paths = getFsClient().listRoots();

			fileRefs = new FileRef[paths.length];
			for (int i = 0; i < paths.length; i++) {
				// fileRefs[i] = FileRefImpl.newInstance(this, paths[i].getPathString());
				fileRefs[i] = getFile(paths[i].getPathString());
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
			FilePath[] paths = getFsClient().listFiles(parent.path());

			fileRefs = new FileRef[paths.length];
			for (int i = 0; i < paths.length; i++) {
				// fileRefs[i] = FileRefImpl.newInstance(this, paths[i].getPathString());
				fileRefs[i] = getFile(paths[i].getPathString());
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
	public InputStream getInputStream(FileRef fileRef) throws IOException {
		return new FileRefInputStream(fileRef);
	}

	@Override
	public OutputStream getOutputStream(FileRef fileRef) throws IOException {
		return new FileRefOutputStream(fileRef);
	}

	@Override
	public boolean uploadInputStreamToFsFile(InputStream input, FileRef destFile) throws IOException {
		try {
			return getFsClient().uploadInputStreamToFsFile(input, destFile.path());
		} catch (ClientException e) {
			handleClientException(e);
		}
		return false;
	}

	@Override
	public boolean uploadFileToFsFile(File localFile, FileRef destFile) throws IOException {
		try {
			return getFsClient().uploadFileToFsFile(localFile, destFile.path());
		} catch (ClientException e) {
			handleClientException(e);
		}
		return false;
	}

	@Override
	public boolean uploadFileToFsDirectory(File localFile, FileRef destDir) throws IOException {
		try {
			return getFsClient().uploadFileToFsDirectory(localFile, destDir.path());
		} catch (ClientException e) {
			handleClientException(e);
		}
		return false;
	}

	@Override
	public boolean uploadDirectoryToFsDirectory(File localDir, FileRef destDir, boolean includingSourceDir) throws IOException {
		try {
			return getFsClient().uploadDirectoryToFsDirectory(localDir, destDir.path(), includingSourceDir);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return false;
	}

	@Override
	public boolean downloadFsFileToOutputStream(FileRef sourceFileRef, OutputStream output) throws IOException {
		try {
			return getFsClient().downloadFsFileToOutputStream(sourceFileRef.path(), output);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return false;
	}

	@Override
	public boolean downloadFsFileToFile(FileRef sourceFile, File localFile) throws IOException {
		try {
			return getFsClient().downloadFsFileToFile(sourceFile.path(), localFile);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return false;
	}

	@Override
	public boolean downloadFsFileToDirectory(FileRef sourceFile, File localDir) throws IOException {
		try {
			return getFsClient().downloadFsFileToDirectory(sourceFile.path(), localDir);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return false;
	}

	@Override
	public boolean downloadFsDirectoryToDirectory(FileRef sourceDir, File localDir, boolean includingSourceDir) throws IOException {
		try {
			return getFsClient().downloadFsDirectoryToDirectory(sourceDir.path(), localDir, includingSourceDir);
		} catch (ClientException e) {
			handleClientException(e);
		}
		return false;
	}

}
