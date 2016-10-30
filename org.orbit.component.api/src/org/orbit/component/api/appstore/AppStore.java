package org.orbit.component.api.appstore;

import java.io.File;
import java.io.OutputStream;

import org.origin.common.rest.client.ClientException;

/**
 * App store client interface.
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface AppStore {

	/**
	 * Get apps.
	 * 
	 * @param query
	 * @return
	 * @throws ClientException
	 */
	public AppManifest[] getApps(AppQuery query) throws ClientException;

	/**
	 * Get an app manifest.
	 * 
	 * @param appId
	 * @return
	 * @throws ClientException
	 */
	public AppManifest getApp(String appId) throws ClientException;

	/**
	 * Check whether an app exists.
	 * 
	 * @param appId
	 * @return
	 * @throws ClientException
	 */
	public boolean exists(String appId) throws ClientException;

	/**
	 * Add a new app.
	 * 
	 * @param createAppRequest
	 * @return
	 * @throws ClientException
	 */
	public boolean create(CreateAppRequest createAppRequest) throws ClientException;

	/**
	 * Add a new app and upload the file.
	 * 
	 * @param createAppRequest
	 * @param file
	 * @return
	 * @throws ClientException
	 */
	public boolean create(CreateAppRequest createAppRequest, File file) throws ClientException;

	/**
	 * Upload file to an existing app.
	 * 
	 * @param appId
	 * @param file
	 * @return
	 * @throws ClientException
	 */
	public boolean upload(String appId, File file) throws ClientException;

	/**
	 * Download an app.
	 * 
	 * @param appId
	 * @param file
	 * @throws ClientException
	 */
	public void download(String appId, File file) throws ClientException;

	/**
	 * Download an app.
	 * 
	 * @param appId
	 * @param output
	 * @throws ClientException
	 */
	public void download(String appId, OutputStream output) throws ClientException;

	/**
	 * Delete an app.
	 * 
	 * @param appId
	 * @throws ClientException
	 */
	public boolean delete(String appId) throws ClientException;

}
