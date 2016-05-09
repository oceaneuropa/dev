package com.osgi.example1.fs.server.ws;

import javax.ws.rs.ext.ContextResolver;

import com.osgi.example1.Activator;
import com.osgi.example1.fs.server.service.FileSystem;

public class FileSystemResolver implements ContextResolver<FileSystem> {

	@Override
	public FileSystem getContext(Class<?> clazz) {
		return Activator.getFsService();
	}

}
