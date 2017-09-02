package org.origin.core.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IFile extends IResource {

	boolean create(InputStream input) throws IOException;

	InputStream getInputStream() throws IOException;

	OutputStream getOutputStream() throws IOException;

	void setContent(InputStream source) throws IOException;

	void setContent(byte[] bytes) throws IOException;

}
