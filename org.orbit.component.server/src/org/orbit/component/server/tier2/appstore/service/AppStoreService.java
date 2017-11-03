package org.orbit.component.server.tier2.appstore.service;

import java.io.InputStream;
import java.util.List;

import org.orbit.component.model.tier2.appstore.AppManifestRTO;
import org.orbit.component.model.tier2.appstore.AppQueryRTO;
import org.orbit.component.model.tier2.appstore.AppStoreException;

public interface AppStoreService {

	public String getNamespace();

	public String getName();

	public String getHostURL();

	public String getContextRoot();

	/**
	 * Get apps.
	 * 
	 * @param type
	 * @return
	 * @throws AppStoreException
	 */
	List<AppManifestRTO> getApps(String type) throws AppStoreException;

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
	 * @param appVersion
	 * @return
	 * @throws AppStoreException
	 */
	AppManifestRTO getApp(String appId, String appVersion) throws AppStoreException;

	/**
	 * Check whether an app exists.
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws AppStoreException
	 */
	boolean appExists(String appId, String appVersion) throws AppStoreException;

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
	 * @param appVersion
	 * @return
	 * @throws AppStoreException
	 */
	boolean deleteApp(String appId, String appVersion) throws AppStoreException;

	/**
	 * Download app.
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws AppStoreException
	 */
	byte[] downloadApp(String appId, String appVersion) throws AppStoreException;

	/**
	 * Download app input stream.
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws AppStoreException
	 */
	InputStream downloadAppInputStream(String appId, String appVersion) throws AppStoreException;

	/**
	 * Upload app.
	 * 
	 * @param appId
	 * @param appVersion
	 * @param fileName
	 * @param fileInputStream
	 * @throws AppStoreException
	 */
	boolean uploadApp(String appId, String appVersion, String fileName, InputStream fileInputStream) throws AppStoreException;

}
