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
import org.orbit.platform.sdk.util.IProcessFilter;
import org.origin.common.extensions.core.IExtension;

public interface ProcessManager {

	int createProcess(IExtension extension, Map<Object, Object> properties) throws ProcessException;

	boolean startProcess(int pid, boolean async) throws ProcessException;

	boolean stopProcess(int pid, boolean async) throws ProcessException;

	boolean exitProcess(int pid, boolean async) throws ProcessException;

	ProcessHandler[] getProcessHandlers();

	ProcessHandler[] getProcessHandlers(ProcessHandlerFilter filter);

	ProcessHandler getProcessHandler(int pid);

	IProcess[] getProcesses();

	IProcess[] getProcesses(IProcessFilter filter);

	IProcess getProcess(int pid);

}
