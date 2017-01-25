package org.orbit.fs.server.googledrive;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.orbit.fs.api.FilePath;
import org.orbit.fs.model.FileMetadata;
import org.orbit.fs.server.service.FileSystemServiceImpl;

public class GoogleDriveFS extends FileSystemServiceImpl {

	/**
	 * 
	 * @param config
	 */
	public GoogleDriveFS(GoogleDriveFSConfig config) {
		setConfig(config);
	}

	@Override
	public FileMetadata getFileMetaData(FilePath path) {
		return null;
	}

	@Override
	public FilePath[] listRoots() {
		return null;
	}

	@Override
	public FilePath[] listFiles(FilePath parent) {
		return null;
	}

	@Override
	public boolean exists(FilePath path) {
		return false;
	}

	@Override
	public boolean isDirectory(FilePath path) {
		return false;
	}

	@Override
	public boolean mkdirs(FilePath path) throws IOException {
		return false;
	}

	@Override
	public boolean createNewFile(FilePath path) throws IOException {
		return false;
	}

	@Override
	public boolean delete(FilePath path) throws IOException {
		return false;
	}

	@Override
	public InputStream getInputStream(FilePath path) throws IOException {
		return null;
	}

	@Override
	public FilePath copyInputStreamToFsFile(InputStream inputStream, FilePath destFilePath) throws IOException {
		return null;
	}

	@Override
	public FilePath copyFileToFsFile(File localFile, FilePath destFilePath) throws IOException {
		return null;
	}

	@Override
	public FilePath copyFileToFsDirectory(File localFile, FilePath destDirPath) throws IOException {
		return null;
	}

	@Override
	public boolean copyDirectoryToFsDirectory(File localDir, FilePath destDirPath, boolean includingSourceDir) throws IOException {
		return false;
	}

}
