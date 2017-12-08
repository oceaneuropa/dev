package org.orbit.os.server.apps;

import java.nio.file.Path;
import java.util.List;

import org.orbit.app.AppManifest;
import org.origin.common.runtime.Problem;

public interface AppsManager {

	void start() throws AppException;

	void stop() throws AppException;

	AppManifest[] getInstalledApps();

	boolean isInstalled(String appId, String appVersion);

	AppManifest getInstalledApp(String appId, String appVersion);

	AppManifest install(Path appArchivePath) throws AppException;

	AppManifest uninstall(String appId, String appVersion) throws AppException;

	AppHandler[] getAppHandlers();

	AppHandler getAppHandler(String appId, String appVersion);

	List<Problem> getProblems();

}

// AppManifest install(String appId, String appVersion) throws AppException;
