package com.osgi.example1.fs.server.service.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.io.FileUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.osgi.example1.fs.common.FileMetadata;
import com.osgi.example1.fs.common.Path;
import com.osgi.example1.fs.server.service.FileSystem;
import com.osgi.example1.fs.server.service.FileSystemConfiguration;

public class LocalFileSystem implements FileSystem {

	protected LocalFileSystemConfiguration config;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();
	protected ServiceRegistration<?> serviceReg;

	/**
	 * 
	 * @param config
	 */
	public LocalFileSystem(LocalFileSystemConfiguration config) {
		this.config = config;
	}

	@Override
	public void start() {
		BundleContext bundleContext = getAdapter(BundleContext.class);
		if (bundleContext != null) {
			// Register as a service
			Hashtable<String, Object> props = new Hashtable<String, Object>();
			this.serviceReg = bundleContext.registerService(FileSystem.class, this, props);
		}
	}

	@Override
	public void stop() {
		// Unregister the service
		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}
	}

	protected File getHomeDirectory() {
		return this.config.getHomeDirectory();
	}

	@Override
	public FileSystemConfiguration getConfiguration() {
		return this.config;
	}

	@Override
	public FileMetadata getFileMetaData(Path path) {
		File file = new File(getHomeDirectory(), path.getPathString());
		return toMetaData(file);
	}

	@Override
	public Path[] listRoots() {
		File[] rootFiles = getHomeDirectory().listFiles();
		Path[] paths = new Path[rootFiles.length];
		for (int i = 0; i < rootFiles.length; i++) {
			paths[i] = new Path(Path.SEPARATOR + rootFiles[i].getName());
		}
		return paths;
	}

	@Override
	public Path[] listFiles(Path parent) {
		File parentFile = new File(getHomeDirectory(), parent.getPathString());
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
		File file = new File(getHomeDirectory(), path.getPathString());
		return (file != null && file.exists()) ? true : false;
	}

	@Override
	public boolean isDirectory(Path path) {
		FileMetadata metadata = getFileMetaData(path);
		return (metadata != null && metadata.isDirectory()) ? true : false;
	}

	@Override
	public boolean mkdirs(Path path) throws IOException {
		File file = new File(getHomeDirectory(), path.getPathString());
		if (!file.exists()) {
			return file.mkdirs();
		}
		return false;
	}

	@Override
	public boolean createNewFile(Path path) throws IOException {
		File file = new File(getHomeDirectory(), path.getPathString());
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
		File file = new File(getHomeDirectory(), path.getPathString());
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
		File file = new File(getHomeDirectory(), path.getPathString());
		return new FileInputStream(file);
	}

	@Override
	public Path copyInputStreamToFsFile(InputStream inputStream, Path destFilePath) throws IOException {
		// Check inputStream
		if (inputStream == null) {
			throw new IOException("InputStream is null.");
		}

		// Check target file
		File destFile = new File(getHomeDirectory(), destFilePath.getPathString());
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
	public Path copyFileToFsFile(File localFile, Path destFilePath) throws IOException {
		// Check source file
		if (!localFile.exists()) {
			throw new IOException("Source '" + localFile + "' does not exist.");
		}
		if (localFile.isDirectory()) {
			throw new IOException("Source '" + localFile + "' exists but is a directory.");
		}

		// Check target file
		File destFile = new File(getHomeDirectory(), destFilePath.getPathString());
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
	public Path copyFileToFsDirectory(File localFile, Path destDirPath) throws IOException {
		// Check source file
		if (!localFile.exists()) {
			throw new IOException("Local file '" + localFile + "' does not exist.");
		}
		if (localFile.isDirectory()) {
			throw new IOException("Local file '" + localFile + "' exists but is a directory.");
		}

		// Check target directory
		File destDir = new File(getHomeDirectory(), destDirPath.getPathString());
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

		return new Path(destDirPath, localFile.getName());
	}

	@Override
	public boolean copyDirectoryToFsDirectory(File localDir, Path destDirPath, boolean includingSourceDir) throws IOException {
		// Check source directory
		if (!localDir.exists()) {
			throw new IOException("Local directory '" + localDir + "' does not exist.");
		}
		if (!localDir.isDirectory()) {
			throw new IOException("Local directory '" + localDir + "' exists but is not a directory.");
		}

		// Check target directory
		File destDir = new File(getHomeDirectory(), destDirPath.getPathString());
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

	/**
	 * Convert java.io.File to a FileMetaData.
	 * 
	 * @param file
	 * @return
	 */
	protected FileMetadata toMetaData(File file) {
		FileMetadata metaData = new FileMetadata();

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

	/** implement IAdaptable interface */
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

}
