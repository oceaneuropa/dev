package org.orbit.component.runtime.tier3.nodecontrol.service;

import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.common.rest.server.WebServiceAware;
import org.origin.core.resources.IWorkspace;

public interface NodeManagementService extends WebServiceAware {

	WSEditPolicies getEditPolicies();

	String getName();

	String getHome();

	IWorkspace getNodeWorkspace();

}
