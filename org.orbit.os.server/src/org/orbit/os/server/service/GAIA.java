package org.orbit.os.server.service;

import java.util.Map;

import org.orbit.os.server.apps.AppsManager;
import org.origin.common.rest.editpolicy.WSEditPolicies;

public interface GAIA {

	String getOSName();

	String getOSVersion();

	String getNamespace();

	String getName();

	String getLabel();

	String getHostURL();

	String getContextRoot();

	String getHome();

	void updateProperties(Map<Object, Object> configProps);

	void start();

	void stop();

	AppsManager getAppsManager();

	WSEditPolicies getEditPolicies();

}
