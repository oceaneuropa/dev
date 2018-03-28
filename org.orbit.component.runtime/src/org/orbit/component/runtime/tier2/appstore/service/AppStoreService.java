package org.orbit.component.runtime.tier2.appstore.service;

import java.io.InputStream;
import java.util.List;

import org.orbit.component.model.tier2.appstore.AppManifestRTO;
import org.orbit.component.model.tier2.appstore.AppQueryRTO;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.WebServiceAware;

public interface AppStoreService extends WebServiceAware {

	public String getName();

	/**
	 * Get apps.
	 * 
	 * @param type
	 * @return
	 * @throws ServerException
	 */
	List<AppManifestRTO> getApps(String type) throws ServerException;

	/**
	 * Get apps.
	 * 
	 * @param query
	 * @return
	 * @throws ServerException
	 */
	List<AppManifestRTO> getApps(AppQueryRTO query) throws ServerException;

	/**
	 * Get an app.
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws ServerException
	 */
	AppManifestRTO getApp(String appId, String appVersion) throws ServerException;

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
	AppManifestRTO addApp(AppManifestRTO newAppRequest) throws ServerException;

	/**
	 * Update an app.
	 * 
	 * @param updateAppRequest
	 * @return
	 * @throws ServerException
	 */
	boolean updateApp(AppManifestRTO updateAppRequest) throws ServerException;

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
	byte[] downloadApp(String appId, String appVersion) throws ServerException;

	/**
	 * Download app input stream.
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws ServerException
	 */
	InputStream downloadAppInputStream(String appId, String appVersion) throws ServerException;

	/**
	 * Upload app.
	 * 
	 * @param appId
	 * @param appVersion
	 * @param fileName
	 * @param fileInputStream
	 * @throws ServerException
	 */
	boolean uploadApp(String appId, String appVersion, String fileName, InputStream fileInputStream) throws ServerException;

}
