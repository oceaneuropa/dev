package org.nb.drive.hdfs;

import org.nb.drive.api.DriveConfig;

public class HdfsConfig implements DriveConfig {

	protected String rootPath;

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

}
