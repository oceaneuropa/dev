package org.origin.core.workspace;

public interface WorkspaceRoot extends IContainer {

	/**
	 * 
	 * @return
	 */
	public IProject[] getProjects();

	/**
	 * 
	 * @param projectId
	 * @return
	 */
	public IProject getProject(String projectId);

}
