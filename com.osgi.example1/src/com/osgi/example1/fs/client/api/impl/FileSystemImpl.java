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

	protected FileSystemClient getClient() {
		return this.config.getFileSystemClient();
	}

	@Override
	public FileRef[] listRootFiles() {
		FileRef[] fileRefs = null;
		try {
			Path[] paths = getClient().listRootFiles();

			fileRefs = new FileRef[paths.length];
			for (int i = 0; i < paths.length; i++) {
				fileRefs[i] = FileRef.newInstance(this, paths[i].getPathString());
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		if (fileRefs == null) {
			fileRefs = EMPTY_FILES;
		}
		return fileRefs;
	}

	@Override
	public FileRef[] listFiles(FileRef parent) {
		FileRef[] fileRefs = null;
		try {
			Path[] paths = getClient().listFiles(parent.path());

			fileRefs = new FileRef[paths.length];
			for (int i = 0; i < paths.length; i++) {
				fileRefs[i] = FileRef.newInstance(this, paths[i].getPathString());
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		if (fileRefs == null) {
			fileRefs = EMPTY_FILES;
		}
		return fileRefs;
	}

	@Override
	public boolean mkdirs(FileRef dir) {
		return false;
	}

	@Override
	public boolean createNewFile(FileRef file) throws IOException {
		return false;
	}

	@Override
	public boolean delete(FileRef file) throws IOException {
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
	public FileRef uploadLocalFileToFile(File localFile, FileRef destFile) throws IOException {
		return null;
	}

	@Override
	public FileRef uploadLocalFileToDirectory(File localFile, FileRef destDir) throws IOException {
		return null;
	}

	@Override
	public boolean uploadLocalDirectoryToDirectory(File localDir, FileRef destDir, boolean includingSourceDir) throws IOException {
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
