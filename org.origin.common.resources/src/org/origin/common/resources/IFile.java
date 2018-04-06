package org.origin.common.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IFile extends IResource {

	InputStream getInputStream() throws IOException;

	OutputStream getOutputStream() throws IOException;

	boolean create(InputStream input) throws IOException;

	void setContents(InputStream input) throws IOException;

	void setContents(byte[] bytes) throws IOException;

}
