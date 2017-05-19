package org.orbit.component.api.tier3.transferagent.request;

public class DeleteNodeRequest {

	protected String nodeId;
	protected boolean force;

	public String getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public boolean isForce() {
		return this.force;
	}

	public void setForce(boolean force) {
		this.force = force;
	}

}
