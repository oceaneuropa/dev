package com.osgi.example1.fs.server.service.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

import com.osgi.example1.fs.common.Configuration;
import com.osgi.example1.fs.common.Path;
import com.osgi.example1.fs.common.dto.FileMetaData;
import com.osgi.example1.fs.server.service.FileSystem;

public class LocalFileSystem implements FileSystem {

	protected LocalFileSystemConfiguration config;

	/**
	 * 
	 * @param config
	 */
	public LocalFileSystem(LocalFileSystemConfiguration config) {
		this.config = config;
	}

	protected File getHomeDirectory() {
		return this.config.getHomeDirectory();
	}

	@Override
	public Configuration getConfiguration() {
		return this.config;
	}

	@Override
	public FileMetaData getFileMetaData(Path path) {
		File file = new File(getHomeDirectory(), path.getName());
		return toMetaData(file);
	}

	@Override
	public Path[] listRootFiles() {
		File[] rootFiles = getHomeDirectory().listFiles();
		Path[] paths = new Path[rootFiles.length];
		for (int i = 0; i < rootFiles.length; i++) {
			paths[i] = new Path(Path.SEPARATOR + rootFiles[i].getName());
		}
		return paths;
	}

	@Override
	public Path[] listFiles(Path parent) {
		File parentFile = new File(getHomeDirectory(), parent.getName());
		if (parentFile.isFile()) {
			return new Path[0];
		}
		File[] subFiles = parentFile.listFiles();
		Path[] paths = new Path[subFiles.length];
		for (int i = 0; i < subFiles.length; i++) {
			paths[i] = new Path(parent, subFiles[i].getName());
		}
		return paths;
	}

	@Override
	public boolean exists(Path path) {
		File file = new File(getHomeDirectory(), path.getName());
		return (file != null && file.exists()) ? true : false;
	}

	@Override
	public boolean mkdirs(Path path) {
		File file = new File(getHomeDirectory(), path.getName());
		if (!file.exists()) {
			return file.mkdirs();
		}
		return false;
	}

	@Override
	public boolean createNewFile(Path path) throws IOException {
		File file = new File(getHomeDirectory(), path.getName());
		if (!file.exists()) {
			if (file.getParentFile() != null && !file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			return file.createNewFile();
		}
		return false;
	}

	@Override
	public boolean delete(Path path) throws IOException {
		File file = new File(getHomeDirectory(), path.getName());
		if (file.exists()) {
			if (file.isDirectory()) {
				FileUtils.deleteDirectory(file);
				return true;
			} else {
				return file.delete();
			}
		}
		return false;
	}

	@Override
	public InputStream getInputStream(Path path) throws IOException {
		File file = new File(getHomeDirectory(), path.getName());
		return new FileInputStream(file);
	}

	@Override
	public Path copyLocalFileToFile(File localFile, Path destFilePath) throws IOException {
		// Check source file
		if (!localFile.exists()) {
			throw new IOException("Source '" + localFile + "' does not exist.");
		}
		if (localFile.isDirectory()) {
			throw new IOException("Source '" + localFile + "' exists but is a directory.");
		}

		// Check target file
		File destFile = new File(getHomeDirectory(), destFilePath.getName());
		if (destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile + "' exists but is a directory.");
		}
		if (destFile.getParentFile() != null && !destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		if (destFile.exists() && !destFile.canWrite()) {
			throw new IOException("Destination '" + destFile + "' exists but is read-only");
		}

		// Copy file
		FileUtils.copyFile(localFile, destFile);

		return destFilePath;
	}

	@Override
	public Path copyLocalFileToDirectory(File localFile, Path destDirPath) throws IOException {
		// Check source file
		if (!localFile.exists()) {
			throw new IOException("Source '" + localFile + "' does not exist.");
		}
		if (localFile.isDirectory()) {
			throw new IOException("Source '" + localFile + "' exists but is a directory.");
		}

		// Check target directory
		File destDir = new File(getHomeDirectory(), destDirPath.getName());
		if (destDir.exists() && !destDir.isDirectory()) {
			throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
		}
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		File destFile = new File(destDir, localFile.getName());
		if (destFile.exists() && !destFile.canWrite()) {
			throw new IOException("Destination '" + destFile + "' exists but is read-only");
		}

		// Copy file
		FileUtils.copyFile(localFile, destFile);

		return new Path(destDirPath, localFile.getName());
	}

	@Override
	public boolean copyLocalDirectoryToDirectory(File localDir, Path destDirPath, boolean includingSourceDir) throws IOException {
		// Check source directory
		if (!localDir.exists()) {
			throw new IOException("Source '" + localDir + "' does not exist.");
		}
		if (!localDir.isDirectory()) {
			throw new IOException("Source '" + localDir + "' exists but is not a directory.");
		}

		// Check target directory
		File destDir = new File(getHomeDirectory(), destDirPath.getName());
		if (destDir.exists() && !destDir.isDirectory()) {
			throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
		}
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		if (includingSourceDir) {
			FileUtils.copyDirectoryToDirectory(localDir, destDir);
		} else {
			FileUtils.copyDirectory(localDir, destDir);
		}
		return true;
	}

	/**
	 * Convert java.io.File to a FileMetaData.
	 * 
	 * @param file
	 * @return
	 */
	protected FileMetaData toMetaData(File file) {
		FileMetaData metaData = new FileMetaData();

		String name = file.getName();
		boolean isDirectory = file.isDirectory();
		boolean isHidden = file.isHidden();
		// String absolutePath = file.getAbsolutePath();
		String path = file.getPath();
		String parentPath = file.getParent();
		boolean exists = file.exists();
		boolean canExecute = file.canExecute();
		boolean canRead = file.canRead();
		boolean canWrite = file.canWrite();
		long length = file.length();
		long lastModified = file.lastModified();

		File homeDir = getHomeDirectory();
		String homePath = homeDir.getPath();
		if (path != null && homePath != null && path.startsWith(homePath) && (path.length() > homePath.length())) {
			path = path.substring(homePath.length());
			if (!path.startsWith(Path.SEPARATOR)) {
				path = Path.SEPARATOR + path;
			}
		}

		metaData.setName(name);
		metaData.setIsDirectory(isDirectory);
		metaData.setHidden(isHidden);
		// metaData.setAbsolutePath(absolutePath);
		metaData.setPath(path);
		metaData.setParentPath(parentPath);
		metaData.setExists(exists);
		metaData.setCanExecute(canExecute);
		metaData.setCanRead(canRead);
		metaData.setCanWrite(canWrite);
		metaData.setLength(length);
		metaData.setLastModified(lastModified);

		return metaData;
	}

}
