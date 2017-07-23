package org.orbit.component.server.tier3.transferagent.service;

import org.origin.common.command.IEditingDomain;

public interface TransferAgentService {

	public IEditingDomain getEditingDomain();

	public String getName();

	public String getHostURL();

	public String getContextRoot();

	public String getHome();

}
