package org.orbit.fs.server.ws;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.fs.common.FileSystem;
import org.orbit.fs.server.Activator;

public class FileSystemResolver implements ContextResolver<FileSystem> {

	@Override
	public FileSystem getContext(Class<?> clazz) {
		return Activator.getFileSystemService();
	}

}
