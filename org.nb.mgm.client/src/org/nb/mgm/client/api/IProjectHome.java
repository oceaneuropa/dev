package org.nb.mgm.client.api;

import java.util.List;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;

public interface IProjectHome extends IAdaptable {

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	public ManagementClient getManagement();

	public IProject getProject();

	// ------------------------------------------------------------------------------------------
	// Auto Update Attributes
	// ------------------------------------------------------------------------------------------
	public boolean isAutoUpdate();

	public void setAutoUpdate(boolean autoUpdate);

	public boolean update() throws ClientException;

	// ------------------------------------------------------------------------------------------
	// Attribute
	// ------------------------------------------------------------------------------------------
	public String getId();

	public String getName();

	public void setName(String name) throws ClientException;

	public String getDescription();

	public void setDescription(String description) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// ProjectHome
	// ------------------------------------------------------------------------------------------
	/**
	 * Get deployment Home.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public IHome getDeploymentHome() throws ClientException;

	/**
	 * Set deployment Home.
	 * 
	 * @param home
	 * @return
	 * @throws ClientException
	 */
	public boolean setDeploymentHome(String homeId) throws ClientException;

	/**
	 * Remove deployment Home.
	 * 
	 * @param projectHomeId
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	public boolean removeDeploymentHome(String homeId) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// ProjectNode
	// ------------------------------------------------------------------------------------------
	/**
	 * Get ProjectNodes in the ProjectHome.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<IProjectNode> getProjectNodes() throws ClientException;

	/**
	 * Get ProjectNode.
	 * 
	 * @param projectNodeId
	 * @return
	 * @throws ClientException
	 */
	public IProjectNode getProjectNode(String projectNodeId) throws ClientException;

	/**
	 * Add a ProjectNode to the ProjectHome.
	 * 
	 * @param projectNodeId
	 * @param name
	 * @param description
	 * @throws ClientException
	 */
	public IProjectNode addProjectNode(String projectNodeId, String name, String description) throws ClientException;

	/**
	 * Delete ProjectNode from the ProjectHome.
	 * 
	 * @param projectNodeId
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteProjectNode(String projectNodeId) throws ClientException;

}
