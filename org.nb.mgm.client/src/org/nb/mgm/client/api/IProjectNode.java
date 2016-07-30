package org.nb.mgm.client.api;

import java.util.List;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;

public interface IProjectNode extends IAdaptable {

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	public ManagementClient getManagement();

	public IProject getProject();

	public IProjectHome getProjectHome();

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
	// Software
	// ------------------------------------------------------------------------------------------
	/**
	 * Get a list of Software installed on the ProjectNode.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<ISoftware> getSoftware() throws ClientException;

	/**
	 * Install Software to ProjectNode.
	 * 
	 * @param softwareId
	 * @throws ClientException
	 */
	public boolean installSoftware(String softwareId) throws ClientException;

	/**
	 * Uninstall Software from ProjectNode.
	 * 
	 * @param softwareId
	 * @throws ClientException
	 */
	public boolean uninstallSoftware(String softwareId) throws ClientException;

}
