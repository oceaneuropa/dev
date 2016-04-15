package org.nb.drive.hdfs;

import org.nb.drive.api.Drive;
import org.nb.drive.api.IFile;

public class HdfsDrive implements Drive {

	protected HdfsConfig config;

	/**
	 * 
	 * @param config
	 */
	public HdfsDrive(HdfsConfig config) {
		this.config = config;
	}

	public HdfsConfig getConfig() {
		return this.config;
	}

	@Override
	public IFile[] listRoots() {
		return null;
	}

}
