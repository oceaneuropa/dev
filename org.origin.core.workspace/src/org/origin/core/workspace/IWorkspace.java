package org.origin.core.workspace;

import java.io.File;
import java.io.IOException;

public interface IWorkspace extends IContainer {

	/**
	 * Create a resource.
	 * 
	 * @param container
	 * @param memberFile
	 * @return
	 */
	public IResource createResource(IContainer container, File memberFile);

	/**
	 * Create a workspace.
	 * 
	 * @param description
	 * @throws IOException
	 */
	public void create(IWorkspaceDescription description) throws IOException;

	/**
	 * Get workspace description.
	 * 
	 * @return
	 */
	public IWorkspaceDescription getDescription();

	/**
	 * Set workspace description.
	 * 
	 * @param description
	 */
	public void setDescription(IWorkspaceDescription description);

	/**
	 * Delete the workspace.
	 */
	public void delete();

	/**
	 * Get projects in the workspace.
	 * 
	 * @return
	 */
	public IProject[] getProjects();

	/**
	 * Get project handle.
	 * 
	 * @param projectId
	 * @return
	 */
	public IProject getProject(String projectId);

}
