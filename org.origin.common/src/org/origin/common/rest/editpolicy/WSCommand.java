package org.origin.common.rest.editpolicy;

import javax.ws.rs.core.Response;

import org.origin.common.rest.model.Request;

public interface WSCommand {

	Response execute(Request request) throws Exception;

}
