package org.orbit.component.api.tier1.registry;

import java.util.Map;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;

/**
 * @see com.sun.jna.platform.win32.Advapi32Util (jna-platform-4.1.0.jar)
 *
 *      <pre>
 * 		/FileSystem/{fs_id}/	Key; Type; Value;	
 * 		/Software/{app_id}/		Key; Type; Value;
 *      </pre>
 * 
 *      <pre>
 *      Types:
 *      string,
 *      boolean,
 *      int,
 *      long,
 *      float,
 *      double,
 *      date
 *      </pre>
 * 
 */
public interface Registry extends IAdaptable {

	String getName();

	String getURL();

	Map<String, Object> getProperties();

	void update(Map<String, Object> properties);

	boolean ping();

	// setString();

	/**
	 * Get the properties in a path.
	 * 
	 * @param userId
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	Map<String, String> getProperties(String userId, EPath path) throws ClientException;

	/**
	 * Get a property in a path.
	 * 
	 * @param userId
	 * @param path
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	String getProperty(String userId, EPath path, String name) throws ClientException;

	/**
	 * Set the properties to a path.
	 * 
	 * @param userId
	 * @param path
	 * @param properties
	 * @throws ClientException
	 */
	void setProperties(String userId, EPath path, Map<String, String> properties) throws ClientException;

	/**
	 * Set a property to a path.
	 * 
	 * @param userId
	 * @param path
	 * @param name
	 * @param value
	 * @throws ClientException
	 */
	void setProperty(String userId, EPath path, String name, String value) throws ClientException;

	/**
	 * Remove a property from a path.
	 * 
	 * @param path
	 * @param name
	 * @throws ClientException
	 */
	void removeProperty(String userId, EPath path, String name) throws ClientException;

	/**
	 * Remove the properties from a path.
	 * 
	 * @param userId
	 * @param path
	 * @throws ClientException
	 */
	void removeAll(String userId, EPath path) throws ClientException;

	/**
	 * Remove all properties from this registry.
	 * 
	 * @param userId
	 * @throws ClientException
	 */
	void removeAll(String userId) throws ClientException;

	boolean close() throws ClientException;

}
