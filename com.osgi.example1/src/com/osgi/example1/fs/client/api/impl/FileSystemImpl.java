package com.osgi.example1.fs.client.api.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.osgi.example1.fs.client.api.FileRef;
import com.osgi.example1.fs.client.api.FileSystem;
import com.osgi.example1.fs.client.api.FileSystemConfiguration;
import com.osgi.example1.fs.common.Path;

public class FileSystemImpl implements FileSystem {

	protected FileSystemConfiguration config;

	/**
	 * 
	 * @param config
	 */
	public FileSystemImpl(FileSystemConfiguration config) {
		this.config = config;

	}

	@Override
	public FileRef[] listRootFiles() {
		return null;
	}

	@Override
	public FileRef[] listFiles(FileRef parent) {
		return null;
	}

	@Override
	public boolean exists(FileRef file) {
		return false;
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
