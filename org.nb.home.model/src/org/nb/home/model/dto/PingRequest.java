package org.nb.home.model.dto;

import org.origin.common.rest.model.Request;

public class PingRequest extends Request {

	public static final String LABEL = "ping";

	public PingRequest() {
		setLabel(LABEL);
	}

}
