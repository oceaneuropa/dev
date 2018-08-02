package org.orbit.component.model.tier3.nodecontrol;

import org.origin.common.rest.model.Request;

public class StopNodeRequest extends Request {

	public static final String REQUEST_NAME = "stop_node";

	protected String nodeName;

	public StopNodeRequest() {
		super(REQUEST_NAME);
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

}
