package org.orbit.fs.common.local;

import java.io.File;

import org.orbit.fs.common.FileSystemConfiguration;

public class LocalFileSystemConfig extends FileSystemConfiguration {

	protected File homeDir;
	protected LocalFileSystemHelper helper;

	public LocalFileSystemConfig(File homeDir) {
		check(homeDir);
		this.homeDir = homeDir;
		this.helper = new LocalFileSystemHelper();
	}

	public File getHomeDirectory() {
		return homeDir;
	}

	public void setHomeDirectory(File homeDir) {
		check(homeDir);
		this.homeDir = homeDir;
	}

	protected void check(File homeDirectory) {
		if (homeDirectory == null) {
			throw new IllegalArgumentException("homeDirectory is null.");
		}
		if (!homeDirectory.isDirectory()) {
			throw new IllegalArgumentException("homeDirectory is not a directory.");
		}
	}

	public LocalFileSystemHelper getLocalFileSystemHelper() {
		return this.helper;
	}

}
