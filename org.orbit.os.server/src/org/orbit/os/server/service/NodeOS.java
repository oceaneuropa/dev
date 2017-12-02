package org.orbit.os.server.service;

import java.util.Map;

import org.orbit.os.server.apps.AppsManager;

public interface NodeOS {

	String getOSName();

	String getOSVersion();

	String getName();

	String getLabel();

	String getHostURL();

	String getContextRoot();

	String getHome();

	void updateProperties(Map<Object, Object> configProps);

	void start();

	boolean isStarted();

	void stop();

	AppsManager getAppsManager();

}
