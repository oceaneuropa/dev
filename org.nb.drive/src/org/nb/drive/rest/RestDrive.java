package org.nb.drive.rest;

import org.nb.drive.api.Drive;
import org.nb.drive.api.IFile;

public class RestDrive implements Drive {

	protected RestDriveConfig config;

	public RestDrive(RestDriveConfig config) {
		this.config = config;
	}

	@Override
	public IFile[] listRoots() {
		return null;
	}

}
