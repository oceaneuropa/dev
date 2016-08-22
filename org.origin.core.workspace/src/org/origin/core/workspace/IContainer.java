package org.origin.core.workspace;

import java.io.IOException;

public interface IContainer extends IResource {

	public IResource[] members() throws IOException;

}
