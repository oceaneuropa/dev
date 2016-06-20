package org.nb.mgm.client.api;

import java.util.List;
import java.util.Map;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;

public interface Home extends IAdaptable {

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	public Management getManagement();

	public Machine getMachine();

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

	public String getUrl();

	public void setUrl(String url) throws ClientException;

	public String getDescription();

	public void setDescription(String description) throws ClientException;

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
