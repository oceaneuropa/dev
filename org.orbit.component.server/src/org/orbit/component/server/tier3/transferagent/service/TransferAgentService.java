package org.orbit.component.server.tier3.transferagent.service;

import org.origin.common.command.IEditingDomain;
import org.origin.core.resources.IWorkspace;

public interface TransferAgentService {

	IEditingDomain getEditingDomain();

	String getName();

	String getHostURL();

	String getContextRoot();

	String getHome();

	IWorkspace getNodespaceRoot();

}
