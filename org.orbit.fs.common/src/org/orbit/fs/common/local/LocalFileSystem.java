package org.orbit.fs.common.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.orbit.fs.api.FilePath;
import org.orbit.fs.common.FileSystemImpl;
import org.orbit.fs.model.FileMetadata;
import org.origin.common.io.FileUtil;
import org.origin.common.resource.IPath;

public class LocalFileSystem extends FileSystemImpl {

	/**
	 * 
	 * @param config
	 */
	public LocalFileSystem(LocalFileSystemConfig config) {
		setConfig(config);
	}

	protected File getHomeDir() {
		File homeDir = null;
		if (getConfig() instanceof LocalFileSystemConfig) {
			homeDir = ((LocalFileSystemConfig) getConfig()).getHomeDirectory();
		}
		return homeDir;
	}

	protected LocalFileSystemHelper getLocalFileSystemHelper() {
		return ((LocalFileSystemConfig) getConfig()).getLocalFileSystemHelper();
	}

	@Override
	public FileMetadata getFileMetaData(IPath path) {
		File file = new File(getHomeDir(), path.getPathString());
		return getLocalFileSystemHelper().toMetaData(getHomeDir(), file);
	}

	@Override
	public IPath[] listRoots() {
		File[] rootFiles = getHomeDir().listFiles();
		IPath[] paths = new IPath[rootFiles.length];
		for (int i = 0; i < rootFiles.length; i++) {
			paths[i] = new FilePath(FilePath.SEPARATOR + rootFiles[i].getName());
		}
		return paths;
	}

	@Override
	public IPath[] listFiles(IPath parent) {
		File parentFile = new File(getHomeDir(), parent.getPathString());
		if (parentFile.isFile()) {
			return new IPath[0];
		}
		File[] subFiles = parentFile.listFiles();
		IPath[] paths = new IPath[subFiles.length];
		for (int i = 0; i < subFiles.length; i++) {
			paths[i] = new FilePath(parent, subFiles[i].getName());
		}
		return paths;
	}

	@Override
	public boolean exists(IPath path) {
		File file = new File(getHomeDir(), path.getPathString());
		return (file != null && file.exists()) ? true : false;
	}

	@Override
	public boolean isDirectory(IPath path) {
		FileMetadata metadata = getFileMetaData(path);
		return (metadata != null && metadata.isDirectory()) ? true : false;
	}

	@Override
	public boolean mkdirs(IPath path) throws IOException {
		File file = new File(getHomeDir(), path.getPathString());
		if (!file.exists()) {
			return file.mkdirs();
		}
		return false;
	}

	@Override
	public boolean createNewFile(IPath path) throws IOException {
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
	public boolean delete(IPath path) throws IOException {
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
	public InputStream getInputStream(IPath path) throws IOException {
		File file = new File(getHomeDir(), path.getPathString());
		return new FileInputStream(file);
	}

	@Override
	public IPath copyInputStreamToFsFile(InputStream inputStream, IPath destFilePath) throws IOException {
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
	public IPath copyFileToFsFile(File localFile, IPath destFilePath) throws IOException {
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
	public IPath copyFileToFsDirectory(File localFile, IPath destDirPath) throws IOException {
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
	public boolean copyDirectoryToFsDirectory(File localDir, IPath destDirPath, boolean includingSourceDir) throws IOException {
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
