package org.origin.core.workspace;

import java.io.IOException;
import java.io.InputStream;

public interface IFile extends IResource {

	public InputStream getContents() throws IOException;

}
