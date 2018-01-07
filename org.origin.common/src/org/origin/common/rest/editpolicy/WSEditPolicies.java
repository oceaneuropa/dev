package org.origin.common.rest.editpolicy;

import java.util.List;

import org.origin.common.rest.model.Request;

public interface WSEditPolicies {

	Class<?>[] getServiceClasses();

	<S> S getService(Class<S> clazz);

	void setService(Class<?> clazz, Object service);

	List<WSEditPolicy> getEditPolicies();

	WSEditPolicy getEditPolicy(String id);

	boolean installEditPolicy(WSEditPolicy editPolicy);

	boolean uninstallEditPolicy(WSEditPolicy editPolicy);

	WSEditPolicy uninstallEditPolicy(String id);

	WSCommand getCommand(Request request);

}
