package org.orbit.os.server.app;

import java.nio.file.Path;
import java.util.List;

import org.orbit.app.AppManifest;
import org.origin.common.runtime.Problem;

public interface AppsManager {

	void start() throws AppException;

	void stop() throws AppException;

	AppManifest[] getInstalledApps();

	boolean isAppInstalled(String appId, String appVersion);

	AppManifest getInstalledApp(String appId, String appVersion);

	AppManifest installApp(Path appArchivePath) throws AppException;

	AppManifest installApp(String appId, String appVersion) throws AppException;

	AppManifest uninstallApp(String appId, String appVersion) throws AppException;

	AppHandler[] getAppHandlers();

	AppHandler getAppHandler(String appId, String appVersion);

	List<Problem> getProblems();

}
