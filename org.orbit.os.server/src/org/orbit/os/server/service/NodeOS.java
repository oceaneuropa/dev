package org.orbit.os.server.service;

import java.util.Map;

public interface NodeOS {

	public String getOSName();

	public String getOSVersion();

	public String getName();

	public String getHostURL();

	public String getContextRoot();

	void updateProperties(Map<Object, Object> configProps);

	void start();

	void stop();

	AppsManager getAppsManager();

}
