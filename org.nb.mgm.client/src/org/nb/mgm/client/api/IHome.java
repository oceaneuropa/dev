package org.nb.mgm.client.api;

import java.util.List;
import java.util.Map;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientConfigurationAware;
import org.origin.common.rest.client.ClientException;

public interface IHome extends IAdaptable, ClientConfigurationAware {

	// ------------------------------------------------------------------------------------------
	// ClientConfiguration
	// ------------------------------------------------------------------------------------------
	public ClientConfiguration getClientConfiguration() throws ClientException;

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	public ManagementClient getManagement();

	public IMachine getMachine();

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
	// Properties
	// ------------------------------------------------------------------------------------------
	/**
	 * Get Home properties.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public Map<String, Object> getProperties() throws ClientException;

	/**
	 * Set Home property.
	 * 
	 * @param propName
	 * @param propValue
	 * @return
	 * @throws ClientException
	 */
	public boolean setProperty(String propName, Object propValue) throws ClientException;

	/**
	 * Set Home properties.
	 * 
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public boolean setProperties(Map<String, Object> properties) throws ClientException;

	/**
	 * Remove Home property.
	 * 
	 * @param propertyName
	 * @return
	 * @throws ClientException
	 */
	public boolean removeProperty(String propertyName) throws ClientException;

	/**
	 * Remove Home properties.
	 * 
	 * @param propertyNames
	 * @return
	 * @throws ClientException
	 */
	public boolean removeProperties(List<String> propertyNames) throws ClientException;

}
