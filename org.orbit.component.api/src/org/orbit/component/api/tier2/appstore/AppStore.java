package org.orbit.component.api.tier2.appstore;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Map;

import org.orbit.component.api.tier2.appstore.request.AppQuery;
import org.orbit.component.api.tier2.appstore.request.CreateAppRequest;
import org.orbit.component.api.tier2.appstore.request.UpdateAppRequest;
import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;

/**
 * App store client interface.
 *
 */
public interface AppStore extends IAdaptable {

	String getName();

	String getURL();

	Map<String, Object> getProperties();

	void update(Map<String, Object> properties);

	boolean ping();

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
	 * @param appVersion
	 * @return
	 * @throws ClientException
	 */
	AppManifest getApp(String appId, String appVersion) throws ClientException;

	/**
	 * Check whether an app exists.
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws ClientException
	 */
	boolean exists(String appId, String appVersion) throws ClientException;

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
	 * @param filePath
	 * @return
	 * @throws ClientException
	 */
	boolean create(CreateAppRequest createAppRequest, Path filePath) throws ClientException;

	/**
	 * Update an app.
	 * 
	 * @param updateAppRequest
	 * @return
	 * @throws ClientException
	 */
	boolean update(UpdateAppRequest updateAppRequest) throws ClientException;

	/**
	 * Delete an app.
	 * 
	 * @param appId
	 * @param appVersion
	 * @throws ClientException
	 */
	boolean delete(String appId, String appVersion) throws ClientException;

	/**
	 * Upload app archive.
	 * 
	 * @param appId
	 * @param appVersion
	 * @param filePath
	 * @return
	 * @throws ClientException
	 */
	boolean uploadAppArchive(String appId, String appVersion, Path filePath) throws ClientException;

	/**
	 * Download app archive.
	 * 
	 * @param appId
	 * @param appVersion
	 * @param output
	 * @throws ClientException
	 */
	void downloadAppArchive(String appId, String appVersion, OutputStream output) throws ClientException;

	boolean close() throws ClientException;

}
