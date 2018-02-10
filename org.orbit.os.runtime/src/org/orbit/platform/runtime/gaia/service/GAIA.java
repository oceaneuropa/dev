package org.orbit.platform.runtime.gaia.service;

import java.util.Map;

import org.orbit.platform.runtime.gaia.world.Worlds;
import org.origin.common.rest.editpolicy.WSEditPolicies;

public interface GAIA {

	String getName();

	String getHostURL();

	String getContextRoot();

	String getHome();

	void updateProperties(Map<Object, Object> configProps);

	void start();

	void stop();

	WSEditPolicies getEditPolicies();

	Worlds getWorlds();

}

// String getOSName();
// String getOSVersion();
// String getNamespace();
// String getLabel();