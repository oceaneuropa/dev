package org.orbit.component.api.tier2.appstore;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

import org.orbit.component.api.tier2.appstore.request.AppQuery;
import org.orbit.component.api.tier2.appstore.request.CreateAppRequest;
import org.orbit.component.api.tier2.appstore.request.UpdateAppRequest;
import org.origin.common.rest.client.ClientException;

/**
 * App store client interface.
 *
 */
public interface AppStore {

	/**
	 * Get service name.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Get service URL.
	 * 
	 * @return
	 */
	String getURL();

	/**
	 * Get configuration properties.
	 * 
	 * @return
	 */
	Map<String, Object> getProperties();

	/**
	 * Update configuration properties.
	 * 
	 * @param properties
	 */
	void update(Map<String, Object> properties);

	/**
	 * Ping the service.
	 * 
	 * @return
	 */
	public boolean ping();

	/**
	 * Get apps.
	 * 
	 * @param query
	 * @return
	 * @throws ClientException
	 */
	AppManifest[] getApps(AppQuery query) throws ClientException;

	/**
	 * Get an app manifest.
	 * 
	 * @param appId
	 * @return
	 * @throws ClientException
	 */
	AppManifest getApp(String appId) throws ClientException;

	/**
	 * Check whether an app exists.
	 * 
	 * @param appId
	 * @return
	 * @throws ClientException
	 */
	boolean exists(String appId) throws ClientException;

	/**
	 * Add a new app.
	 * 
	 * @param createAppRequest
	 * @return
	 * @throws ClientException
	 */
	boolean create(CreateAppRequest createAppRequest) throws ClientException;

	/**
	 * Add a new app and upload the file.
	 * 
	 * @param createAppRequest
	 * @param file
	 * @return
	 * @throws ClientException
	 */
	boolean create(CreateAppRequest createAppRequest, File file) throws ClientException;

	/**
	 * Update an app.
	 * 
	 * @param updateAppRequest
	 * @return
	 * @throws ClientException
	 */
	boolean update(UpdateAppRequest updateAppRequest) throws ClientException;

	/**
	 * Upload file to an existing app.
	 * 
	 * @param appId
	 * @param file
	 * @return
	 * @throws ClientException
	 */
	boolean upload(String appId, File file) throws ClientException;

	/**
	 * Download an app.
	 * 
	 * @param appId
	 * @param file
	 * @throws ClientException
	 */
	void download(String appId, File file) throws ClientException;

	/**
	 * Download an app.
	 * 
	 * @param appId
	 * @param output
	 * @throws ClientException
	 */
	void download(String appId, OutputStream output) throws ClientException;

	/**
	 * Delete an app.
	 * 
	 * @param appId
	 * @throws ClientException
	 */
	boolean delete(String appId) throws ClientException;

}
