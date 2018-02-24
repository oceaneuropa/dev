package org.orbit.component.runtime.tier1.config.service;

import java.util.List;
import java.util.Map;

import org.orbit.component.model.tier1.config.EPath;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.server.ServerException;

public interface ConfigRegistry {

	// ---------------------------------------------------------------------------------------------------------
	// Path
	// ---------------------------------------------------------------------------------------------------------
	/**
	 * Get paths.
	 * 
	 * @return
	 * @throws ServerException
	 */
	public List<EPath> getPaths() throws ServerException;

	/**
	 * Check whether path exists.
	 * 
	 * @param path
	 * @return
	 * @throws ServerException
	 */
	public boolean hasPath(EPath path) throws ServerException;

	/**
	 * Add a path.
	 * 
	 * @param path
	 * @param description
	 * @return
	 * @throws ServerException
	 */
	public boolean addPath(EPath path, String description) throws ServerException;

	/**
	 * Update a path.
	 * 
	 * @param path
	 * @param newPath
	 * @return
	 * @throws ServerException
	 */
	public boolean updatePath(EPath path, EPath newPath) throws ServerException;

	/**
	 * Update a path's description.
	 * 
	 * @param path
	 * @param description
	 * @return
	 * @throws ServerException
	 */
	public boolean updatePathDescription(EPath path, String description) throws ServerException;

	/**
	 * Remove a path and all properties in the path.
	 * 
	 * @param path
	 * @return
	 * @throws ServerException
	 */
	public boolean removePath(EPath path) throws ServerException;

	/**
	 * Remove all paths and all properties in each path.
	 * 
	 * @return
	 */
	public boolean removeAllPaths() throws ServerException;

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
	public Map<String, String> getProperties(EPath path) throws ServerException;

	/**
	 * Get a property in a path.
	 * 
	 * @param path
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public String getProperty(EPath path, String name) throws ServerException;

	/**
	 * Set a property to a path.
	 * 
	 * @param path
	 * @param name
	 * @param value
	 * @throws ClientException
	 */
	public boolean setProperty(EPath path, String name, String value) throws ServerException;

	/**
	 * Remove a property from a path.
	 * 
	 * @param path
	 * @param name
	 * @throws ClientException
	 */
	public boolean removeProperty(EPath path, String name) throws ServerException;

	/**
	 * Remove the properties from a path.
	 * 
	 * @param path
	 * @throws ClientException
	 */
	public boolean removeProperties(EPath path) throws ServerException;

}
