package org.orbit.component.api.tier3.nodecontrol;

public interface NodeStatus {

	boolean isActivate();

	void setActivate(boolean isActivate);

	String getRuntimeState();

	void setRuntimeState(String runtimeState);

}
