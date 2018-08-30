package org.orbit.component.api.tier1.configregistry;

import java.util.Map;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

/*
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
public interface ConfigRegistryClient extends ServiceClient {

	/**
	 * Get the properties in a path.
	 * 
	 * @param accountId
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	Map<String, String> getProperties(String accountId, EPath path) throws ClientException;

	/**
	 * Get a property in a path.
	 * 
	 * @param accountId
	 * @param path
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	String getProperty(String accountId, EPath path, String name) throws ClientException;

	/**
	 * Set the properties to a path.
	 * 
	 * @param accountId
	 * @param path
	 * @param properties
	 * @throws ClientException
	 */
	void setProperties(String accountId, EPath path, Map<String, String> properties) throws ClientException;

	/**
	 * Set a property to a path.
	 * 
	 * @param accountId
	 * @param path
	 * @param name
	 * @param value
	 * @throws ClientException
	 */
	void setProperty(String accountId, EPath path, String name, String value) throws ClientException;

	/**
	 * Remove a property from a path.
	 * 
	 * @param path
	 * @param name
	 * @throws ClientException
	 */
	void removeProperty(String accountId, EPath path, String name) throws ClientException;

	/**
	 * Remove the properties from a path.
	 * 
	 * @param accountId
	 * @param path
	 * @throws ClientException
	 */
	void removeAll(String accountId, EPath path) throws ClientException;

	/**
	 * Remove all properties from this registry.
	 * 
	 * @param accountId
	 * @throws ClientException
	 */
	void removeAll(String accountId) throws ClientException;

}
