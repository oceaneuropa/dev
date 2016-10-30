package org.orbit.component.api.configregistry;

import java.util.Map;

import org.origin.common.rest.client.ClientException;

public interface ConfigurationRegistry {

	/**
	 * Get the properties in a path.
	 * 
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public Map<String, String> getProperties(EPath path) throws ClientException;

	/**
	 * Get a property in a path.
	 * 
	 * @param path
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public String getProperty(EPath path, String name) throws ClientException;

	/**
	 * Set the properties to a path.
	 * 
	 * @param path
	 * @param properties
	 * @throws ClientException
	 */
	public void setProperties(EPath path, Map<String, String> properties) throws ClientException;

	/**
	 * Set a property to a path.
	 * 
	 * @param path
	 * @param name
	 * @param value
	 * @throws ClientException
	 */
	public void setProperty(EPath path, String name, String value) throws ClientException;

	/**
	 * Remove a property from a path.
	 * 
	 * @param path
	 * @param name
	 * @throws ClientException
	 */
	public void removeProperty(EPath path, String name) throws ClientException;

	/**
	 * Remove the properties from a path.
	 * 
	 * @param path
	 * @throws ClientException
	 */
	public void removeAll(EPath path) throws ClientException;

	/**
	 * Remove all properties from this registry.
	 * 
	 * @throws ClientException
	 */
	public void removeAll() throws ClientException;

}
