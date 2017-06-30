package org.orbit.component.model.tier3.transferagent.request;

import org.origin.common.rest.model.Request;

public class StopNodeRequest extends Request {

	public static final String NAME = "stop_node";

	protected String nodeName;

	public StopNodeRequest() {
		super(NAME);
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

}
