package com.osgi.example1.fs.client.api;

import com.osgi.example1.fs.client.api.impl.FileRefImpl;
import com.osgi.example1.fs.common.Path;

public abstract class FileRef {

	public static FileRef newInstance(FileSystem fs, String pathString) {
		return new FileRefImpl(fs, pathString);
	}

	public static FileRef newInstance(FileSystem fs, FileRef parent, String child) {
		return new FileRefImpl(fs, parent, child);
	}

	public static FileRef newInstance(FileSystem fs, String parent, FileRef child) {
		return new FileRefImpl(fs, parent, child);
	}

	public static FileRef newInstance(FileSystem fs, String parentPath, String childPath) {
		return new FileRefImpl(fs, parentPath, childPath);
	}

	public static FileRef[] listRoots(FileSystem fs) {
		return fs.listRootFiles();
	}

	public static FileRef[] listFiles(FileRef parent) {
		FileSystem fs = parent.getFileSystem();
		return fs.listFiles(parent);
	}

	public abstract FileSystem getFileSystem();

	public abstract Path path();

	public abstract String getPath();

	public abstract String getParent();

	public abstract FileRef getParentFile();

	public abstract String getName();

	public abstract boolean isDirectory();

	public abstract boolean isHidden();

	public abstract boolean exists();

	public abstract boolean canExecute();

	public abstract boolean canRead();

	public abstract boolean canWrite();

	public abstract long getLength();

	public abstract long getLastModified();

}
