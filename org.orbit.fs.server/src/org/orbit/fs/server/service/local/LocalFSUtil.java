package org.orbit.fs.server.service.local;

import java.io.File;

import org.orbit.fs.api.FilePath;
import org.orbit.fs.model.FileMetadata;

public class LocalFSUtil {

	/**
	 * Convert java.io.File to a FileMetaData.
	 * 
	 * @param file
	 * @return
	 */
	public static FileMetadata toMetaData(File homeDir, File file) {
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

		// File homeDir = getHomeDir();
		String homePath = homeDir.getPath();
		if (path != null && homePath != null && path.startsWith(homePath) && (path.length() > homePath.length())) {
			path = path.substring(homePath.length());
			if (!path.startsWith(FilePath.SEPARATOR)) {
				path = FilePath.SEPARATOR + path;
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
