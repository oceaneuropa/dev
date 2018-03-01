/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.runtime.processes;

import java.util.Map;

import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.IProcessFilter;
import org.orbit.platform.sdk.extension.IProgramExtension;

public interface ProcessManager {

	ProcessHandler createProcess(IProgramExtension extension, Map<String, Object> properties) throws ProcessException;

	void exitProcess(int pid, boolean sync) throws ProcessException;

	ProcessHandler[] getProcessHandlers();

	ProcessHandler[] getProcessHandlers(ProcessHandlerFilter filter);

	ProcessHandler getProcessHandler(int pid);

	IProcess[] getProcesses();

	IProcess[] getProcesses(IProcessFilter filter);

	IProcess getProcess(int pid);

}
