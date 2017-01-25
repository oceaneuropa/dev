package org.orbit.fs.server.service.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.orbit.fs.api.FilePath;
import org.orbit.fs.model.FileMetadata;
import org.orbit.fs.server.service.FileSystemServiceImpl;
import org.origin.common.io.FileUtil;

public class LocalFS extends FileSystemServiceImpl {

	/**
	 * 
	 * @param config
	 */
	public LocalFS(LocalFSConfig config) {
		setConfig(config);
	}

	protected File getHomeDir() {
		File homeDir = null;
		if (getConfig() instanceof LocalFSConfig) {
			homeDir = ((LocalFSConfig) getConfig()).getHomeDirectory();
		}
		return homeDir;
	}

	@Override
	public FileMetadata getFileMetaData(FilePath path) {
		File file = new File(getHomeDir(), path.getPathString());
		return LocalFSUtil.toMetaData(getHomeDir(), file);
	}

	@Override
	public FilePath[] listRoots() {
		File[] rootFiles = getHomeDir().listFiles();
		FilePath[] paths = new FilePath[rootFiles.length];
		for (int i = 0; i < rootFiles.length; i++) {
			paths[i] = new FilePath(FilePath.SEPARATOR + rootFiles[i].getName());
		}
		return paths;
	}

	@Override
	public FilePath[] listFiles(FilePath parent) {
		File parentFile = new File(getHomeDir(), parent.getPathString());
		if (parentFile.isFile()) {
			return new FilePath[0];
		}
		File[] subFiles = parentFile.listFiles();
		FilePath[] paths = new FilePath[subFiles.length];
		for (int i = 0; i < subFiles.length; i++) {
			paths[i] = new FilePath(parent, subFiles[i].getName());
		}
		return paths;
	}

	@Override
	public boolean exists(FilePath path) {
		File file = new File(getHomeDir(), path.getPathString());
		return (file != null && file.exists()) ? true : false;
	}

	@Override
	public boolean isDirectory(FilePath path) {
		FileMetadata metadata = getFileMetaData(path);
		return (metadata != null && metadata.isDirectory()) ? true : false;
	}

	@Override
	public boolean mkdirs(FilePath path) throws IOException {
		File file = new File(getHomeDir(), path.getPathString());
		if (!file.exists()) {
			return file.mkdirs();
		}
		return false;
	}

	@Override
	public boolean createNewFile(FilePath path) throws IOException {
		File file = new File(getHomeDir(), path.getPathString());
		if (!file.exists()) {
			if (file.getParentFile() != null && !file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			return file.createNewFile();
		}
		return false;
	}

	@Override
	public boolean delete(FilePath path) throws IOException {
		File file = new File(getHomeDir(), path.getPathString());
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
	public InputStream getInputStream(FilePath path) throws IOException {
		File file = new File(getHomeDir(), path.getPathString());
		return new FileInputStream(file);
	}

	@Override
	public FilePath copyInputStreamToFsFile(InputStream inputStream, FilePath destFilePath) throws IOException {
		// Check inputStream
		if (inputStream == null) {
			throw new IOException("InputStream is null.");
		}

		// Check target file
		File destFile = new File(getHomeDir(), destFilePath.getPathString());
		if (destFile.isDirectory()) {
			throw new IOException("Path '" + destFile + "' exists but is a directory.");
		}
		if (destFile.getParentFile() != null && !destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		if (destFile.exists() && !destFile.canWrite()) {
			throw new IOException("Path '" + destFile + "' exists but is read-only.");
		}

		// Copy input stream to file.
		FileUtil.copyInputStreamToFile(inputStream, destFile);

		return destFilePath;
	}

	@Override
	public FilePath copyFileToFsFile(File localFile, FilePath destFilePath) throws IOException {
		// Check source file
		if (!localFile.exists()) {
			throw new IOException("Source '" + localFile + "' does not exist.");
		}
		if (localFile.isDirectory()) {
			throw new IOException("Source '" + localFile + "' exists but is a directory.");
		}

		// Check target file
		File destFile = new File(getHomeDir(), destFilePath.getPathString());
		if (destFile.isDirectory()) {
			throw new IOException("Path '" + destFile + "' exists but is a directory.");
		}
		if (destFile.getParentFile() != null && !destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		if (destFile.exists() && !destFile.canWrite()) {
			throw new IOException("Path '" + destFile + "' exists but is read-only");
		}

		// Copy file
		FileUtils.copyFile(localFile, destFile);

		return destFilePath;
	}

	@Override
	public FilePath copyFileToFsDirectory(File localFile, FilePath destDirPath) throws IOException {
		// Check source file
		if (!localFile.exists()) {
			throw new IOException("Local file '" + localFile + "' does not exist.");
		}
		if (localFile.isDirectory()) {
			throw new IOException("Local file '" + localFile + "' exists but is a directory.");
		}

		// Check target directory
		File destDir = new File(getHomeDir(), destDirPath.getPathString());
		if (destDir.exists() && !destDir.isDirectory()) {
			throw new IllegalArgumentException("Path '" + destDir + "' is not a directory.");
		}
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		File destFile = new File(destDir, localFile.getName());
		if (destFile.exists() && !destFile.canWrite()) {
			throw new IOException("Path '" + destFile + "' exists but is read-only.");
		}

		// Copy file
		FileUtils.copyFile(localFile, destFile);

		return new FilePath(destDirPath, localFile.getName());
	}

	@Override
	public boolean copyDirectoryToFsDirectory(File localDir, FilePath destDirPath, boolean includingSourceDir) throws IOException {
		// Check source directory
		if (!localDir.exists()) {
			throw new IOException("Local directory '" + localDir + "' does not exist.");
		}
		if (!localDir.isDirectory()) {
			throw new IOException("Local directory '" + localDir + "' exists but is not a directory.");
		}

		// Check target directory
		File destDir = new File(getHomeDir(), destDirPath.getPathString());
		if (destDir.exists() && !destDir.isDirectory()) {
			throw new IllegalArgumentException("Path '" + destDir + "' exists but is not a directory.");
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

}
