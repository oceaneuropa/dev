package org.origin.common.rest.editpolicy;

import java.util.List;

import org.origin.common.rest.model.Request;

public interface WSEditPolicies {

	List<WSEditPolicy> getEditPolicies();

	boolean installEditPolicy(WSEditPolicy editPolicy);

	boolean uninstallEditPolicy(WSEditPolicy editPolicy);

	WSCommand getCommand(Request request);

}
