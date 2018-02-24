package org.orbit.platform.runtime.processes;

import org.orbit.platform.sdk.IProcess;

public interface ProcessHandler {

	int getPID();

	String getExtensionTypeId();

	String getExtensionId();

	IProcess getProcess();

}
