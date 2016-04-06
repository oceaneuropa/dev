package org.nb.drive.rest.server;

import javax.ws.rs.ext.ContextResolver;

import org.nb.drive.Activator;

public class DriveServiceResolver implements ContextResolver<DriveService> {

	@Override
	public DriveService getContext(Class<?> clazz) {
		return Activator.getDriveService();
	}

}
