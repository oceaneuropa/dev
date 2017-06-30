package org.orbit.component.model.tier3.transferagent.request;

import org.origin.common.rest.model.Request;

public class CreateNodeRequest extends Request {

	public static final String NAME = "create_node";

	protected String nodeName;

	public CreateNodeRequest() {
		super(NAME);
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

}
