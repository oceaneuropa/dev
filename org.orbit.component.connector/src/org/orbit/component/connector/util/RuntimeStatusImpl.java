package org.orbit.component.connector.util;

import org.orbit.component.api.RuntimeStatus;

public class RuntimeStatusImpl implements RuntimeStatus {

	protected boolean isOnline = false;
	protected String runtimeState;

	public boolean isOnline() {
		return this.isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public String getRuntimeState() {
		return this.runtimeState;
	}

	public void setRuntimeState(String runtimeState) {
		this.runtimeState = runtimeState;
	}

}
