package org.orbit.os.server.service;

import org.orbit.app.AppManifest;

public interface AppsManager {

	void start();

	void stop();

	AppManifest[] getInstalledApps();

	boolean addApp(AppManifest appManifest);

	boolean removeApp(String appId, String appVersion);

	boolean appExists(String appId, String appVersion);

	public AppHandler[] getAppHandlers();

	public AppHandler getAppHandler(String appId, String appVersion);

}
