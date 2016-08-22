package org.origin.core.workspace;

import java.io.File;

public interface Workspace {

	/**
	 * 
	 * @return
	 */
	public WorkspaceRoot getRoot();

	/**
	 * 
	 * @param container
	 * @param file
	 * @return
	 */
	public IResource createResource(IContainer container, File file);

}
