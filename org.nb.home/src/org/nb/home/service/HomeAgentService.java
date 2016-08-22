package org.nb.home.service;

import java.util.List;

import org.nb.home.model.exception.HomeException;
import org.nb.home.model.runtime.Workspace;
import org.nb.home.model.runtime.config.WorkspaceConfig;
import org.origin.common.command.IEditingDomain;

public interface HomeAgentService {

	// ------------------------------------------------------------------------------------------
	// EditingDomain
	// ------------------------------------------------------------------------------------------
	public IEditingDomain getEditingDomain();

	// ------------------------------------------------------------------------------------------
	// ping
	// ------------------------------------------------------------------------------------------
	/**
	 * Ping
	 * 
	 * @return
	 */
	public int ping() throws HomeException;

	// ------------------------------------------------------------------------------------------
	// Workspace
	// ------------------------------------------------------------------------------------------
	/**
	 * Get workspaces.
	 * 
	 * @param managementId
	 *            the id of a management that created the workspaces.
	 * @return
	 * @throws HomeException
	 */
	public List<Workspace> getWorkspaces(String managementId) throws HomeException;

	/**
	 * Get a workspace.
	 * 
	 * @param managementId
	 *            the id of a management that created the workspace.
	 * @param name
	 * @return
	 * @throws HomeException
	 */
	public Workspace getWorkspace(String managementId, String name) throws HomeException;

	/**
	 * Create a workspace.
	 * 
	 * @param newWorkspaceRequest
	 * @return
	 * @throws HomeException
	 */
	public Workspace createWorkspace(WorkspaceConfig newWorkspaceRequest) throws HomeException;

	/**
	 * Delete a workspace.
	 * 
	 * @param managementId
	 * @param name
	 * @throws HomeException
	 */
	public boolean deleteWorkspace(String managementId, String name) throws HomeException;

	// ------------------------------------------------------------------------------------------
	// Project
	// ------------------------------------------------------------------------------------------
	/**
	 * 
	 * @param projectId
	 * @return
	 * @throws HomeException
	 */
	public boolean projectExists(String projectId) throws HomeException;

	/**
	 * 
	 * @param projectId
	 * @return
	 * @throws HomeException
	 */
	public boolean createProject(String projectId) throws HomeException;

	/**
	 * 
	 * @param projectId
	 * @return
	 * @throws HomeException
	 */
	public boolean deleteProject(String projectId) throws HomeException;

}
