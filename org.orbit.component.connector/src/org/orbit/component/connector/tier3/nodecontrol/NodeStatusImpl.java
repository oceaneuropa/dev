package org.orbit.component.connector.tier3.nodecontrol;

import org.orbit.component.api.tier3.nodecontrol.NodeStatus;

public class NodeStatusImpl implements NodeStatus {

	protected boolean isActivate = false;
	protected String runtimeState;

	public boolean isActivate() {
		return this.isActivate;
	}

	public void setActivate(boolean isActivate) {
		this.isActivate = isActivate;
	}

	public String getRuntimeState() {
		return this.runtimeState;
	}

	public void setRuntimeState(String runtimeState) {
		this.runtimeState = runtimeState;
	}

}
