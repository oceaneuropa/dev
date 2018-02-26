/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.runtime.processes;

import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.extension.IProgramExtension;

public class ProcessHandlerImpl implements ProcessHandler {

	protected ProcessManager processManager;
	protected IProgramExtension extension;
	protected IPlatformContext context;
	protected IProcess process;
	protected RUNTIME_STATE runtimeState = RUNTIME_STATE.STOPPED;

	/**
	 * 
	 * @param processManager
	 * @param extension
	 * @param context
	 * @param process
	 */
	public ProcessHandlerImpl(ProcessManager processManager, IProgramExtension extension, IPlatformContext context, IProcess process) {
		this.processManager = processManager;
		this.extension = extension;
		this.context = context;
		this.process = process;
	}

	@Override
	public IProgramExtension getExtension() {
		return this.extension;
	}

	@Override
	public IProcess getProcess() {
		return this.process;
	}

	@Override
	public RUNTIME_STATE getRuntimeState() {
		return this.runtimeState;
	}

	public void setRuntimeState(RUNTIME_STATE runtimeState) {
		this.runtimeState = runtimeState;
	}

	public synchronized void doStart() {
		ServiceActivator serviceActivator = this.extension.getAdapter(ServiceActivator.class);
		if (serviceActivator != null) {
			try {
				serviceActivator.start(this.context, this.process);
				setRuntimeState(RUNTIME_STATE.STARTED);

			} catch (Exception e) {
				setRuntimeState(RUNTIME_STATE.START_FAILED);
				e.printStackTrace();
			}
		}
	}

	public synchronized void doStop() {
		ServiceActivator serviceActivator = this.extension.getAdapter(ServiceActivator.class);
		if (serviceActivator != null) {
			try {
				serviceActivator.stop(this.context, this.process);
				setRuntimeState(RUNTIME_STATE.STOPPED);

			} catch (Exception e) {
				setRuntimeState(RUNTIME_STATE.STOP_FAILED);
				e.printStackTrace();
			}
		}
	}

}

// @Override
// public void stop(boolean sync) {
// int pid = this.process.getPID();
// this.processManager.stopProcess(pid, sync);
// }

// @Override
// public void stop() {
// Platform platform = this.getAdapter(Platform.class);
// if (platform != null) {
// platform.stop(this);
// }
// }
