package org.orbit.component.api.util;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.tier2.appstore.AppQuery;
import org.orbit.component.api.tier2.appstore.AppStoreClient;
import org.orbit.component.api.tier2.appstore.CreateAppRequest;
import org.orbit.component.api.tier2.appstore.UpdateAppRequest;
import org.origin.common.rest.client.ClientException;

public class AppStoreUtil {

	protected static AppManifest[] EMPTY_APPS = new AppManifest[0];

	/**
	 * 
	 * @param appStoreUrl
	 * @return
	 */
	public static AppStoreClient getClient(String accessToken) {
		String appStoreUrl = ComponentsConfigPropertiesHandler.getInstance().getAppStoreURL();
		AppStoreClient appStoreClient = ComponentClients.getInstance().getAppStoreClient(appStoreUrl, accessToken);
		return appStoreClient;
	}

	/**
	 * 
	 * @param accessToken
	 * @return
	 * @throws ClientException
	 */
	public static AppManifest[] getApps(String accessToken) throws ClientException {
		AppManifest[] appManifests = null;
		AppStoreClient appStore = getClient(accessToken);
		if (appStore != null) {
			AppQuery query = new AppQuery();
			appManifests = appStore.getApps(query);
		}
		if (appManifests == null) {
			appManifests = EMPTY_APPS;
		}
		return appManifests;
	}

	/**
	 * 
	 * @param accessToken
	 * @param appId
	 * @return
	 * @throws ClientException
	 */
	public static AppManifest[] getApps(String accessToken, String appId) throws ClientException {
		AppManifest[] appManifests = null;
		AppStoreClient appStore = getClient(accessToken);
		if (appStore != null) {
			AppQuery query = new AppQuery();
			query.setAppId(appId, AppQuery.OPERATOR__EQUAL);
			appManifests = appStore.getApps(query);
		}
		if (appManifests == null) {
			appManifests = EMPTY_APPS;
		}
		return appManifests;
	}

	/**
	 * 
	 * @param accessToken
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws ClientException
	 */
	public static AppManifest getApp(String accessToken, String appId, String appVersion) throws ClientException {
		AppManifest appManifest = null;
		AppStoreClient appStore = getClient(accessToken);
		if (appStore != null) {
			appManifest = appStore.getApp(appId, appVersion);
		}
		return appManifest;
	}

	/**
	 * 
	 * @param accessToken
	 * @param id
	 * @param version
	 * @param type
	 * @param name
	 * @param desc
	 * @param fileName
	 * @return
	 * @throws ClientException
	 */
	public static boolean addApp(String accessToken, String id, String version, String type, String name, String desc, String fileName) throws ClientException {
		boolean succeed = false;
		AppStoreClient appStore = getClient(accessToken);
		if (appStore != null) {
			CreateAppRequest createAppRequest = new CreateAppRequest();
			createAppRequest.setAppId(id);
			createAppRequest.setAppVersion(version);
			createAppRequest.setType(type);
			createAppRequest.setName(name);
			createAppRequest.setDescription(desc);
			createAppRequest.setFileName(fileName);

			succeed = appStore.create(createAppRequest);
		}
		return succeed;
	}

	/**
	 * 
	 * @param accessToken
	 * @param id
	 * @param appId
	 * @param appVersion
	 * @param type
	 * @param name
	 * @param desc
	 * @param fileName
	 * @return
	 * @throws ClientException
	 */
	public static boolean updateApp(String accessToken, int id, String appId, String appVersion, String type, String name, String desc, String fileName) throws ClientException {
		boolean succeed = false;
		AppStoreClient appStore = getClient(accessToken);
		if (appStore != null) {
			UpdateAppRequest updateAppRequest = new UpdateAppRequest();
			updateAppRequest.setId(id);
			updateAppRequest.setAppId(appId);
			updateAppRequest.setAppVersion(appVersion);
			updateAppRequest.setType(type);
			updateAppRequest.setName(name);
			updateAppRequest.setDescription(desc);
			updateAppRequest.setFileName(fileName);

			succeed = appStore.update(updateAppRequest);
		}
		return succeed;
	}

	/**
	 * 
	 * @param accessToken
	 * @param id
	 * @param version
	 * @return
	 * @throws ClientException
	 */
	public static boolean deleteApp(String accessToken, String id, String version) throws ClientException {
		boolean succeed = false;
		AppStoreClient appStore = getClient(accessToken);
		if (appStore != null) {
			succeed = appStore.delete(id, version);
		}
		return succeed;
	}

	/**
	 * 
	 * @param accessToken
	 * @param id
	 * @param appId
	 * @param appVersion
	 * @param files
	 * @return
	 * @throws ClientException
	 */
	public static boolean uploadAppFile(String accessToken, int id, String appId, String appVersion, List<File> files) throws ClientException {
		boolean succeed = false;
		AppStoreClient appStore = getClient(accessToken);
		if (appStore != null) {
			File file = (files != null && !files.isEmpty()) ? files.get(0) : null;
			if (file != null && file.exists()) {
				succeed = appStore.uploadAppArchive(id, appId, appVersion, file.toPath());
			}
		}
		return succeed;
	}

	/**
	 * 
	 * @param accessToken
	 * @param appId
	 * @param appVersion
	 * @param output
	 * @throws ClientException
	 */
	public static void downloadAppFile(String accessToken, String appId, String appVersion, OutputStream output) throws ClientException {
		AppStoreClient appStore = getClient(accessToken);
		if (appStore != null) {
			appStore.downloadAppArchive(appId, appVersion, output);
		}
	}

}

// /**
// *
// * @param appStoreUrl
// * @param accessToken
// * @return
// */
// protected AppStoreClient getAppStoreClient(String appStoreUrl, String accessToken) {
// AppStoreClient appStoreClient = ComponentClients.getInstance().getAppStoreClient(appStoreUrl, accessToken);
// return appStoreClient;
// }
