package com.osgi.example1.fs.server.service.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import com.osgi.example1.fs.common.Configuration;
import com.osgi.example1.fs.common.Path;
import com.osgi.example1.fs.common.dto.FileMetaData;
import com.osgi.example1.fs.server.service.FileSystem;

public class DatabaseFileSystem implements FileSystem {

	protected DatabaseFileSystemConfiguration config;

	/**
	 * 
	 * @param config
	 */
	public DatabaseFileSystem(DatabaseFileSystemConfiguration config) {
		this.config = config;
	}

	protected Connection getConnection() {
		return this.config.getConnection();
	}

	@Override
	public Configuration getConfiguration() {
		return this.config;
	}

	@Override
	public FileMetaData getFileMetaData(Path path) {
		return null;
	}

	@Override
	public Path[] listRootFiles() {
		return null;
	}

	@Override
	public Path[] listFiles(Path parent) {
		return null;
	}

	@Override
	public boolean exists(Path path) {
		return false;
	}

	@Override
	public boolean mkdirs(Path path) {
		return false;
	}

	@Override
	public boolean createNewFile(Path path) throws IOException {
		return false;
	}

	@Override
	public boolean delete(Path path) throws IOException {
		return false;
	}

	@Override
	public InputStream getInputStream(Path path) throws IOException {
		return null;
	}

	@Override
	public Path copyLocalFileToFile(File localFile, Path destFilePath) throws IOException {
		return null;
	}

	@Override
	public Path copyLocalFileToDirectory(File localFile, Path destDirPath) throws IOException {
		return null;
	}

	@Override
	public boolean copyLocalDirectoryToDirectory(File localDir, Path destDirPath, boolean includingSourceDir) throws IOException {
		return false;
	}

}
