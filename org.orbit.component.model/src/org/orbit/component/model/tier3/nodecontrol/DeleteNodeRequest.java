package org.orbit.component.model.tier3.nodecontrol;

import org.origin.common.rest.model.Request;

public class DeleteNodeRequest extends Request {

	public static final String REQUEST_NAME = "delete_node";

	protected String nodeName;

	public DeleteNodeRequest() {
		super(REQUEST_NAME);
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

}
