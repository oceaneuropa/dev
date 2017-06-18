package org.orbit.os.server.service;

import java.util.Map;

import org.orbit.os.server.app.AppsManager;

public interface NodeOS {

	String getOSName();

	String getOSVersion();

	String getName();

	String getLabel();

	String getHostURL();

	String getContextRoot();

	void updateProperties(Map<Object, Object> configProps);

	void start();

	boolean isStarted();

	void stop();

	AppsManager getAppsManager();

}
