package org.orbit.component.connector;

import org.orbit.component.api.RuntimeStatus;

public class RuntimeStatusImpl implements RuntimeStatus {

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
