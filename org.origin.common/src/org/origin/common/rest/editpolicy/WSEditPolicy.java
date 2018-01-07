package org.origin.common.rest.editpolicy;

import org.origin.common.rest.model.Request;

public interface WSEditPolicy {

	String getId();

	<S> S getService(Class<S> clazz);

	void setService(Class<?> clazz, Object service);

	WSCommand getCommand(Request request);

}
