package org.orbit.component.runtime.tier2.appstore.service;

import java.io.InputStream;
import java.util.List;

import org.orbit.component.runtime.model.appstore.AppManifest;
import org.orbit.component.runtime.model.appstore.AppQuery;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.AccessTokenAware;
import org.origin.common.service.WebServiceAware;

public interface AppStoreService extends WebServiceAware, AccessTokenAware {

	/**
	 * Get apps.
	 * 
	 * @param type
	 * @return
	 * @throws ServerException
	 */
	List<AppManifest> getApps(String type) throws ServerException;

	/**
	 * Get apps.
	 * 
	 * @param query
	 * @return
	 * @throws ServerException
	 */
	List<AppManifest> getApps(AppQuery query) throws ServerException;

	/**
	 * Get an app.
	 * 
	 * @param id
	 * @return
	 * @throws ServerException
	 */
	AppManifest getApp(int id) throws ServerException;

	/**
	 * Get an app.
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws ServerException
	 */
	AppManifest getApp(String appId, String appVersion) throws ServerException;

	/**
	 * Check whether an app exists.
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws ServerException
	 */
	boolean appExists(String appId, String appVersion) throws ServerException;

	/**
	 * Add an app.
	 * 
	 * @param newAppRequest
	 * @return
	 * @throws ServerException
	 */
	AppManifest addApp(AppManifest newAppRequest) throws ServerException;

	/**
	 * Update an app.
	 * 
	 * @param updateAppRequest
	 * @return
	 * @throws ServerException
	 */
	boolean updateApp(AppManifest updateAppRequest) throws ServerException;

	/**
	 * Delete an app.
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws ServerException
	 */
	boolean deleteApp(String appId, String appVersion) throws ServerException;

	/**
	 * Download app.
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws ServerException
	 */
	byte[] getContent(String appId, String appVersion) throws ServerException;

	/**
	 * Download app input stream.
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws ServerException
	 */
	InputStream getContentInputStream(String appId, String appVersion) throws ServerException;

	/**
	 * Upload app.
	 * 
	 * @param appId
	 * @param appVersion
	 * @param fileName
	 * @param fileInputStream
	 * @throws ServerException
	 */
	boolean setContent(String appId, String appVersion, String fileName, InputStream fileInputStream) throws ServerException;

}
