package org.orbit.fs.server.googledrive;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.orbit.fs.common.FileSystemImpl;
import org.orbit.fs.model.FileMetadata;
import org.origin.common.resource.IPath;

public class GoogleDriveFS extends FileSystemImpl {

	/**
	 * 
	 * @param config
	 */
	public GoogleDriveFS(GoogleDriveFSConfig config) {
		setConfig(config);
	}

	@Override
	public FileMetadata getFileMetaData(IPath path) {
		return null;
	}

	@Override
	public IPath[] listRoots() {
		return null;
	}

	@Override
	public IPath[] listFiles(IPath parent) {
		return null;
	}

	@Override
	public boolean exists(IPath path) {
		return false;
	}

	@Override
	public boolean isDirectory(IPath path) {
		return false;
	}

	@Override
	public boolean mkdirs(IPath path) throws IOException {
		return false;
	}

	@Override
	public boolean createNewFile(IPath path) throws IOException {
		return false;
	}

	@Override
	public boolean delete(IPath path) throws IOException {
		return false;
	}

	@Override
	public InputStream getInputStream(IPath path) throws IOException {
		return null;
	}

	@Override
	public IPath copyInputStreamToFsFile(InputStream inputStream, IPath destFilePath) throws IOException {
		return null;
	}

	@Override
	public IPath copyFileToFsFile(File localFile, IPath destFilePath) throws IOException {
		return null;
	}

	@Override
	public IPath copyFileToFsDirectory(File localFile, IPath destDirPath) throws IOException {
		return null;
	}

	@Override
	public boolean copyDirectoryToFsDirectory(File localDir, IPath destDirPath, boolean includingSourceDir) throws IOException {
		return false;
	}

}
