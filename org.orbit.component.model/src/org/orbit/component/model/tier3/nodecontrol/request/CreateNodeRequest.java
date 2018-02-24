package org.orbit.component.model.tier3.nodecontrol.request;

import org.origin.common.rest.model.Request;

public class CreateNodeRequest extends Request {

	public static final String REQUEST_NAME = "create_node";

	protected String nodeName;

	public CreateNodeRequest() {
		super(REQUEST_NAME);
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

}
