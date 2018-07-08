package org.orbit.component.api;

public interface RuntimeStatus {

	boolean isActivate();

	void setActivate(boolean isActivate);

	String getRuntimeState();

	void setRuntimeState(String runtimeState);

}
