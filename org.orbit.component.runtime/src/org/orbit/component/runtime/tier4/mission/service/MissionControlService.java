package org.orbit.component.runtime.tier4.mission.service;

import org.origin.common.rest.editpolicy.WSEditPolicies;

public interface MissionControlService {

	WSEditPolicies getEditPolicies();

	String getName();

	String getHostURL();

	String getContextRoot();

}
