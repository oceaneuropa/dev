package org.nb.mgm.client.api;

import java.util.List;
import java.util.Properties;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;

public interface IMetaSector extends IAdaptable {

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	public ManagementClient getManagement();

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
	// MetaSpace
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all MetaSpaces in a MetaSector.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<IMetaSpace> getMetaSpaces() throws ClientException;

	/**
	 * Get MetaSpaces in a MetaSector by query parameters.
	 * 
	 * @param properties
	 *            supported keys are: "name", "filter".
	 * @return
	 * @throws ClientException
	 */
	public List<IMetaSpace> getMetaSpaces(Properties properties) throws ClientException;

	/**
	 * Get MetaSpace by metaSpace Id.
	 * 
	 * @param metaSpaceId
	 * @return
	 * @throws ClientException
	 */
	public IMetaSpace getMetaSpace(String metaSpaceId) throws ClientException;

	/**
	 * Add a MetaSpace to a MetaSector.
	 * 
	 * @param name
	 * @param description
	 * @throws ClientException
	 */
	public IMetaSpace addMetaSpace(String name, String description) throws ClientException;

	/**
	 * Delete MetaSpace from a MetaSector by metaSpace Id.
	 * 
	 * @param metaSpaceId
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteMetaSpace(String metaSpaceId) throws ClientException;

}
