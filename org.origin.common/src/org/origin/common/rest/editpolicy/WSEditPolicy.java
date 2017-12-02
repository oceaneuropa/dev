package org.origin.common.rest.editpolicy;

import org.origin.common.rest.model.Request;

public interface WSEditPolicy {

	<S> S getService(Class<S> clazz);

	<S> void setService(Class<S> clazz, S service);

	WSCommand getCommand(Request request);

}
