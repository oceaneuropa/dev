package org.orbit.sprit.runtime.gaia.service;

import java.util.Map;

import org.orbit.sprit.runtime.gaia.world.Worlds;
import org.origin.common.rest.editpolicy.WSEditPolicies;

public interface GAIA {

	String getName();

	String getHostURL();

	String getContextRoot();

	void updateProperties(Map<Object, Object> configProps);

	WSEditPolicies getEditPolicies();

	Worlds getWorlds();

}

// String getHome();
// String getOSName();
// String getOSVersion();
// String getNamespace();
// String getLabel();