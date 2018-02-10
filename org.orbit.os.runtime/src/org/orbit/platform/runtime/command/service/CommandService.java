package org.orbit.platform.runtime.command.service;

import org.origin.common.rest.editpolicy.WSEditPolicies;

public interface CommandService {

	String getName();

	String getContextRoot();

	void start();

	void stop();

	WSEditPolicies getEditPolicies();

}
