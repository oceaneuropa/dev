package org.orbit.component.api.tier2.appstore;

import java.io.OutputStream;
import java.nio.file.Path;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

/**
 * App store client interface.
 *
 */
public interface AppStoreClient extends ServiceClient {

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
	 * @param id
	 * @param appId
	 * @param appVersion
	 * @param filePath
	 * @return
	 * @throws ClientException
	 */
	boolean uploadAppArchive(int id, String appId, String appVersion, Path filePath) throws ClientException;

	/**
	 * Download app archive.
	 * 
	 * @param appId
	 * @param appVersion
	 * @param output
	 * @throws ClientException
	 */
	boolean downloadAppArchive(String appId, String appVersion, OutputStream output) throws ClientException;

}
