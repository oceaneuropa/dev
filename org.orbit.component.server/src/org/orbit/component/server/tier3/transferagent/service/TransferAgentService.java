package org.orbit.component.server.tier3.transferagent.service;

import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.core.resources.IWorkspace;

public interface TransferAgentService extends WSEditPolicies {

	String getNamespace();

	String getName();

	String getHostURL();

	String getContextRoot();

	String getHome();

	IWorkspace getNodeWorkspace();

}
