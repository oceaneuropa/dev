/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.sdk;

import org.orbit.platform.sdk.util.IProcessFilter;

public interface IProcessManager {

	IProcess[] getProcesses();

	IProcess[] getProcesses(IProcessFilter filter);

	IProcess getProcess(int pid);

}
