package org.orbit.spirit.runtime.gaia.service;

import java.util.Map;

import org.orbit.spirit.runtime.gaia.world.Worlds;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.service.WebServiceAware;

public interface GAIA extends WebServiceAware {

	void updateProperties(Map<Object, Object> configProps);

	ServiceEditPolicies getEditPolicies();

	Worlds getWorlds();

}

// String getHome();
// String getOSName();
// String getOSVersion();
// String getNamespace();
// String getLabel();
