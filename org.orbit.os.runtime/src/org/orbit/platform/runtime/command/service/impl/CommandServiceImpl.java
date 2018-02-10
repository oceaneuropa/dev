package org.orbit.platform.runtime.command.service.impl;

import org.orbit.platform.runtime.command.service.CommandService;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.common.rest.editpolicy.WSEditPoliciesImpl;

public class CommandServiceImpl implements CommandService {

	protected WSEditPolicies wsEditPolicies;

	public CommandServiceImpl() {
		this.wsEditPolicies = new WSEditPoliciesImpl();
		this.wsEditPolicies.setService(CommandService.class, this);
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getContextRoot() {
		return null;
	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	@Override
	public WSEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}
}
