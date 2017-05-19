package org.orbit.component.api.tier1.config;

import java.util.Map;

import org.origin.common.rest.client.ClientException;

public interface ConfigRegistry {

	/**
	 * Get ConfigRegistry name.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Get ConfigRegistry URL.
	 * 
	 * @return
	 */
	String getURL();

	/**
	 * Get properties.
	 * 
	 * @return
	 */
	Map<String, Object> getProperties();

	/**
	 * Update properties.
	 * 
	 * @param properties
	 */
	void update(Map<String, Object> properties);

	/**
	 * Ping the service.
	 * 
	 * @return
	 */
	boolean ping();

	/**
	 * Get the properties in a path.
	 * 
	 * @param userId
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public Map<String, String> getProperties(String userId, EPath path) throws ClientException;

	/**
	 * Get a property in a path.
	 * 
	 * @param userId
	 * @param path
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public String getProperty(String userId, EPath path, String name) throws ClientException;

	/**
	 * Set the properties to a path.
	 * 
	 * @param userId
	 * @param path
	 * @param properties
	 * @throws ClientException
	 */
	public void setProperties(String userId, EPath path, Map<String, String> properties) throws ClientException;

	/**
	 * Set a property to a path.
	 * 
	 * @param userId
	 * @param path
	 * @param name
	 * @param value
	 * @throws ClientException
	 */
	public void setProperty(String userId, EPath path, String name, String value) throws ClientException;

	/**
	 * Remove a property from a path.
	 * 
	 * @param path
	 * @param name
	 * @throws ClientException
	 */
	public void removeProperty(String userId, EPath path, String name) throws ClientException;

	/**
	 * Remove the properties from a path.
	 * 
	 * @param userId
	 * @param path
	 * @throws ClientException
	 */
	public void removeAll(String userId, EPath path) throws ClientException;

	/**
	 * Remove all properties from this registry.
	 * 
	 * @param userId
	 * @throws ClientException
	 */
	public void removeAll(String userId) throws ClientException;

}
