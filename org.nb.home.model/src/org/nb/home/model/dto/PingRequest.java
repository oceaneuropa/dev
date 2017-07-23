package org.nb.home.model.dto;

import org.origin.common.rest.model.Request;

public class PingRequest extends Request {

	public static final String REQUEST_NAME = "ping";

	public PingRequest() {
		super(REQUEST_NAME);
	}

}
