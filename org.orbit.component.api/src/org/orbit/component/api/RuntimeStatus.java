package org.orbit.component.api;

public interface RuntimeStatus {

	boolean isOnline();

	void setOnline(boolean isOnline);

	String getRuntimeState();

	void setRuntimeState(String runtimeState);

}
