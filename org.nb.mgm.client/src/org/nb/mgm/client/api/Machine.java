package org.nb.mgm.client.api;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;

public interface Machine extends IAdaptable {

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

	public String getIpAddress();

	public void setIpAddress(String ipAddress) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// Home
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all Homes in a Machine.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<Home> getHomes() throws ClientException;

	/**
	 * Get Homes in a Machine by query parameters.
	 * 
	 * @param properties
	 *            supported keys are: "name", "url", "status", "filter".
	 * @return
	 * @throws ClientException
	 */
	public List<Home> getHomes(Properties properties) throws ClientException;

	/**
	 * Get Home by home Id.
	 * 
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	public Home getHome(String homeId) throws ClientException;

	/**
	 * Add a Home to a Machine.
	 * 
	 * @param name
	 * @param url
	 * @param description
	 * @throws ClientException
	 */
	public Home addHome(String name, String description, String url) throws ClientException;

	/**
	 * Delete Home from a Machine by home Id.
	 * 
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteHome(String homeId) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------------------------
	/**
	 * Get Machine properties.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public Map<String, Object> getProperties() throws ClientException;

	/**
	 * Set Machine property.
	 * 
	 * @param propName
	 * @param propValue
	 * @return
	 * @throws ClientException
	 */
	public boolean setProperty(String propName, Object propValue) throws ClientException;

	/**
	 * Set Machine properties.
	 * 
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public boolean setProperties(Map<String, Object> properties) throws ClientException;

	/**
	 * Remove Machine properties.
	 * 
	 * @param propertyNames
	 * @return
	 * @throws ClientException
	 */
	public boolean removeProperties(List<String> propertyNames) throws ClientException;

}
