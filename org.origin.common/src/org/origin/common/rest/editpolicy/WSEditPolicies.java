package org.origin.common.rest.editpolicy;

import java.util.List;

import org.origin.common.rest.model.Request;

public interface WSEditPolicies {

	List<WSEditPolicy> getEditPolicies();

	WSEditPolicy getEditPolicy(String id);

	boolean installEditPolicy(WSEditPolicy editPolicy);

	boolean uninstallEditPolicy(WSEditPolicy editPolicy);

	WSEditPolicy uninstallEditPolicy(String id);

	WSCommand getCommand(Request request);

}
