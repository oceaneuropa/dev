package com.osgi.example1.fs.server.service.local;

import java.io.File;

import com.osgi.example1.fs.server.service.FileSystemConfiguration;

public class LocalFileSystemConfiguration extends FileSystemConfiguration {

	protected File homeDirectory;

	public LocalFileSystemConfiguration(File homeDirectory) {
		check(homeDirectory);
		this.homeDirectory = homeDirectory;
	}

	public File getHomeDirectory() {
		return homeDirectory;
	}

	public void setHomeDirectory(File homeDirectory) {
		check(homeDirectory);
		this.homeDirectory = homeDirectory;
	}

	protected void check(File homeDirectory) {
		if (homeDirectory == null) {
			throw new IllegalArgumentException("homeDirectory is null.");
		}
		if (!homeDirectory.isDirectory()) {
			throw new IllegalArgumentException("homeDirectory is not a directory.");
		}
	}

}
