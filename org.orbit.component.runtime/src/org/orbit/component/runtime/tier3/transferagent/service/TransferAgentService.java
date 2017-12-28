package org.orbit.component.runtime.tier3.transferagent.service;

import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.core.resources.IWorkspace;

public interface TransferAgentService {

	String getName();

	String getHostURL();

	String getContextRoot();

	String getHome();

	IWorkspace getNodeWorkspace();

	WSEditPolicies getEditPolicies();

}