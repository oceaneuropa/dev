package org.origin.common.resource.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

import org.origin.common.resource.URIConverter;

public class URIHandlerFileImpl extends URIHandlerImpl {

	protected File getFile(URI uri) {
		return new File(uri);
	}

	@Override
	public boolean isSupported(URI uri) {
		return ("file".equalsIgnoreCase(uri.getScheme())) ? true : false;
	}

	@Override
	public boolean exists(URI uri, Map<?, ?> options) {
		File file = getFile(uri);
		return file.exists();
	}

	@Override
	public boolean createNewResource(URI uri, Map<Object, Object> options) throws IOException {
		File file = getFile(uri);
		if (file.getParentFile() != null && !file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return file.createNewFile();
	}

	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		File file = getFile(uri);
		InputStream inputStream = new FileInputStream(file);
		Map<Object, Object> response = getResponse(options);
		if (response != null) {
			response.put(URIConverter.RESPONSE_TIME_STAMP_PROPERTY, file.lastModified());
		}
		return inputStream;
	}

	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException {
		final File file = getFile(uri);
		String parent = file.getParent();
		if (parent != null) {
			new File(parent).mkdirs();
		}
		final Map<Object, Object> response = getResponse(options);
		OutputStream outputStream = new FileOutputStream(file) {
			@Override
			public void close() throws IOException {
				try {
					super.close();
				} finally {
					if (response != null) {
						response.put(URIConverter.RESPONSE_TIME_STAMP_PROPERTY, file.lastModified());
					}
				}
			}
		};
		return outputStream;
	}

	@Override
	public boolean delete(URI uri, Map<?, ?> options) throws IOException {
		File file = getFile(uri);
		return file.delete();
	}

}
