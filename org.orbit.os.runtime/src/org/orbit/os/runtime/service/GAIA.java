package org.orbit.os.runtime.service;

import java.util.Map;

import org.orbit.os.runtime.programs.ProgramsAndFeatures;
import org.orbit.os.runtime.world.Worlds;
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

	ProgramsAndFeatures getProgramsAndFeatures();

	WSEditPolicies getEditPolicies();

	Worlds getWorlds();

}
