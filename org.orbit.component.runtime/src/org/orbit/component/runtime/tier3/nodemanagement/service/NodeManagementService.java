package org.orbit.component.runtime.tier3.nodemanagement.service;

import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.common.rest.util.WebServiceAware;
import org.origin.core.resources.IWorkspace;

public interface NodeManagementService extends WebServiceAware {

	WSEditPolicies getEditPolicies();

	String getName();

	String getHome();

	IWorkspace getNodeWorkspace();

}
