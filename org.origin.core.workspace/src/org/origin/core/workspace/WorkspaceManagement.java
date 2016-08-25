package org.origin.core.workspace;

import org.origin.common.runtime.IStatus;

public interface WorkspaceManagement {

	/**
	 * Get existing workspaces.
	 * 
	 * @return
	 */
	public IWorkspace[] getWorkspaces();

	/**
	 * Get a workspace.
	 * 
	 * @param name
	 * @return
	 */
	public IWorkspace getWorkspace(String name);

	/**
	 * Create a workspace.
	 * 
	 * @param name
	 * @return
	 */
	public IStatus createWorkspace(String name);

	/**
	 * Delete a workspace.
	 * 
	 * @param workspace
	 * @return
	 */
	public IStatus deleteWorkspace(IWorkspace workspace);

}
