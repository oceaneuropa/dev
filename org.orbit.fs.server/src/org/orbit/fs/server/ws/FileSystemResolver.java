package org.orbit.fs.server.ws;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.fs.server.Activator;
import org.orbit.fs.server.service.FileSystemService;

public class FileSystemResolver implements ContextResolver<FileSystemService> {

	@Override
	public FileSystemService getContext(Class<?> clazz) {
		return Activator.getFileSystemService();
	}

}
