package org.orbit.component.runtime.tier1.config.service;

import java.util.List;
import java.util.Map;

import org.orbit.component.model.tier1.config.ConfigRegistryException;
import org.orbit.component.model.tier1.config.EPath;

public interface ConfigRegistry {

	// ---------------------------------------------------------------------------------------------------------
	// Path
	// ---------------------------------------------------------------------------------------------------------
	/**
	 * Get paths.
	 * 
	 * @return
	 * @throws ConfigRegistryException
	 */
	public List<EPath> getPaths() throws ConfigRegistryException;

	/**
	 * Check whether path exists.
	 * 
	 * @param path
	 * @return
	 * @throws ConfigRegistryException
	 */
	public boolean hasPath(EPath path) throws ConfigRegistryException;

	/**
	 * Add a path.
	 * 
	 * @param path
	 * @param description
	 * @return
	 * @throws ConfigRegistryException
	 */
	public boolean addPath(EPath path, String description) throws ConfigRegistryException;

	/**
	 * Update a path.
	 * 
	 * @param path
	 * @param newPath
	 * @return
	 * @throws ConfigRegistryException
	 */
	public boolean updatePath(EPath path, EPath newPath) throws ConfigRegistryException;

	/**
	 * Update a path's description.
	 * 
	 * @param path
	 * @param description
	 * @return
	 * @throws ConfigRegistryException
	 */
	public boolean updatePathDescription(EPath path, String description) throws ConfigRegistryException;

	/**
	 * Remove a path and all properties in the path.
	 * 
	 * @param path
	 * @return
	 * @throws ConfigRegistryException
	 */
	public boolean removePath(EPath path) throws ConfigRegistryException;

	/**
	 * Remove all paths and all properties in each path.
	 * 
	 * @return
	 */
	public boolean removeAllPaths() throws ConfigRegistryException;

	// ---------------------------------------------------------------------------------------------------------
	// Path properties
	// ---------------------------------------------------------------------------------------------------------
	/**
	 * Get the properties in a path.
	 * 
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public Map<String, String> getProperties(EPath path) throws ConfigRegistryException;

	/**
	 * Get a property in a path.
	 * 
	 * @param path
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public String getProperty(EPath path, String name) throws ConfigRegistryException;

	/**
	 * Set a property to a path.
	 * 
	 * @param path
	 * @param name
	 * @param value
	 * @throws ClientException
	 */
	public boolean setProperty(EPath path, String name, String value) throws ConfigRegistryException;

	/**
	 * Remove a property from a path.
	 * 
	 * @param path
	 * @param name
	 * @throws ClientException
	 */
	public boolean removeProperty(EPath path, String name) throws ConfigRegistryException;

	/**
	 * Remove the properties from a path.
	 * 
	 * @param path
	 * @throws ClientException
	 */
	public boolean removeProperties(EPath path) throws ConfigRegistryException;

}
