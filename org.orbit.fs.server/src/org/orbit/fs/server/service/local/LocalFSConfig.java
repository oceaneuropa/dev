package org.orbit.fs.server.service.local;

import java.io.File;

import org.orbit.fs.server.service.FileSystemServiceConfiguration;

public class LocalFSConfig extends FileSystemServiceConfiguration {

	protected File homeDir;

	public LocalFSConfig(File homeDir) {
		check(homeDir);
		this.homeDir = homeDir;
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

}
