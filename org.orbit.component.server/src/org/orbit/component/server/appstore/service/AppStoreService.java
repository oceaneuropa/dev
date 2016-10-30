package org.orbit.component.server.appstore.service;

import java.io.InputStream;
import java.util.List;

import org.orbit.component.model.appstore.exception.AppStoreException;
import org.orbit.component.model.appstore.runtime.AppManifestRTO;
import org.orbit.component.model.appstore.runtime.AppQueryRTO;

public interface AppStoreService {

	/**
	 * Get apps.
	 * 
	 * @param namespace
	 * @param categoryId
	 * @return
	 * @throws AppStoreException
	 */
	List<AppManifestRTO> getApps(String namespace, String categoryId) throws AppStoreException;

	/**
	 * Get apps.
	 * 
	 * @param query
	 * @return
	 * @throws AppStoreException
	 */
	List<AppManifestRTO> getApps(AppQueryRTO query) throws AppStoreException;

	/**
	 * Get an app.
	 * 
	 * @param appId
	 * @return
	 * @throws AppStoreException
	 */
	AppManifestRTO getApp(String appId) throws AppStoreException;

	/**
	 * Check whether an app exists.
	 * 
	 * @param appId
	 * @return
	 * @throws AppStoreException
	 */
	boolean appExists(String appId) throws AppStoreException;

	/**
	 * Add an app.
	 * 
	 * @param newAppRequest
	 * @return
	 * @throws AppStoreException
	 */
	AppManifestRTO addApp(AppManifestRTO newAppRequest) throws AppStoreException;

	/**
	 * Update an app.
	 * 
	 * @param updateAppRequest
	 * @return
	 * @throws AppStoreException
	 */
	boolean updateApp(AppManifestRTO updateAppRequest) throws AppStoreException;

	/**
	 * Delete an app.
	 * 
	 * @param appId
	 * @return
	 * @throws AppStoreException
	 */
	boolean deleteApp(String appId) throws AppStoreException;

	/**
	 * Download app.
	 * 
	 * @param appId
	 * @return
	 * @throws AppStoreException
	 */
	byte[] downloadApp(String appId) throws AppStoreException;

	/**
	 * Download app input stream.
	 * 
	 * @param appId
	 * @return
	 * @throws AppStoreException
	 */
	InputStream downloadAppInputStream(String appId) throws AppStoreException;

	/**
	 * Upload app.
	 * 
	 * @param appId
	 * @param fileName
	 * @param fileInputStream
	 * @throws AppStoreException
	 */
	boolean uploadApp(String appId, String fileName, InputStream fileInputStream) throws AppStoreException;

}
