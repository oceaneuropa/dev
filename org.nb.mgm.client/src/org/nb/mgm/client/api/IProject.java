package org.nb.mgm.client.api;

import java.util.List;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;

public interface IProject extends IAdaptable {

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	public Management getManagement();

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
	 * Get ProjectHomes in a Project.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<IProjectHome> getProjectHomes() throws ClientException;

	/**
	 * Get ProjectHome.
	 * 
	 * @param projectHomeId
	 * @return
	 * @throws ClientException
	 */
	public IProjectHome getProjectHome(String projectHomeId) throws ClientException;

	/**
	 * Add a ProjectHome to a Project.
	 * 
	 * @param name
	 * @param description
	 * @throws ClientException
	 */
	public IProjectHome addProjectHome(String name, String description) throws ClientException;

	/**
	 * Delete ProjectHome from a Project.
	 * 
	 * @param projectHomeId
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteProjectHome(String projectHomeId) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// ProjectSoftware
	// ------------------------------------------------------------------------------------------
	/**
	 * Get Software in a Project.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<ISoftware> getProjectSoftware() throws ClientException;

	/**
	 * Get Software.
	 * 
	 * @param softwareId
	 * @return
	 * @throws ClientException
	 */
	public ISoftware getProjectSoftware(String softwareId) throws ClientException;

	/**
	 * Add a Software to a Project.
	 * 
	 * @param name
	 * @param description
	 * @throws ClientException
	 */
	public ISoftware addProjectSoftware(String type, String name, String version, String description) throws ClientException;

	/**
	 * Delete Software from a Project.
	 * 
	 * @param softwareId
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteProjectSoftware(String softwareId) throws ClientException;

}
