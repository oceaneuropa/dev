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

	@Override
	public boolean canStart() {
		if (canChangeState(this.runtimeState, RUNTIME_STATE.STARTED) || canChangeState(this.runtimeState, RUNTIME_STATE.START_FAILED)) {
			return true;
		}
		return false;
	}

	@Override
	public synchronized void start() throws ProcessException {
		if (!canStart()) {
			throw new ProcessException("Cannot start process from current state '" + this.runtimeState.name() + "'.");
		}

		ServiceActivator serviceActivator = this.extension.getInterface(ServiceActivator.class);
		if (serviceActivator != null) {
			if (!canChangeState(this.runtimeState, RUNTIME_STATE.STARTED) && !canChangeState(this.runtimeState, RUNTIME_STATE.START_FAILED)) {
				throw new RuntimeException("Cannot start process from '" + this.runtimeState.name() + "' state.");
			}
			try {
				serviceActivator.start(this.context, this.process);
				setRuntimeState(RUNTIME_STATE.STARTED);

			} catch (Exception e) {
				setRuntimeState(RUNTIME_STATE.START_FAILED);
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean canStop() {
		if (canChangeState(this.runtimeState, RUNTIME_STATE.STOPPED) || canChangeState(this.runtimeState, RUNTIME_STATE.STOP_FAILED)) {
			return true;
		}
		return false;
	}

	@Override
	public synchronized void stop() throws ProcessException {
		if (!canStop()) {
			throw new ProcessException("Cannot stop process from current state '" + this.runtimeState.name() + "'.");
		}

		ServiceActivator serviceActivator = this.extension.getInterface(ServiceActivator.class);
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

	/**
	 * 
	 * @param fromState
	 * @param toState
	 * @return
	 */
	protected boolean canChangeState(RUNTIME_STATE fromState, RUNTIME_STATE toState) {
		if (fromState == null) {
			throw new RuntimeException("fromState is null");
		}
		if (toState == null) {
			throw new RuntimeException("toState is null");
		}

		if (RUNTIME_STATE.STOPPED.equals(fromState)) {
			// Stopped -> Started
			// Stopped -> StartFailed
			if (RUNTIME_STATE.STARTED.equals(toState) || RUNTIME_STATE.START_FAILED.equals(toState)) {
				return true;
			}

		} else if (RUNTIME_STATE.STOP_FAILED.equals(fromState)) {
			// StopFailed -> Started
			// StopFailed -> StartFailed
			// StopFailed -> Stopped
			if (RUNTIME_STATE.STARTED.equals(toState) || RUNTIME_STATE.START_FAILED.equals(toState) || RUNTIME_STATE.STOPPED.equals(toState)) {
				return true;
			}

		} else if (RUNTIME_STATE.STARTED.equals(fromState)) {
			// Started -> Stopped
			// Started -> StopFailed
			if (RUNTIME_STATE.STOPPED.equals(toState) || RUNTIME_STATE.STOP_FAILED.equals(toState)) {
				return true;
			}

		} else if (RUNTIME_STATE.START_FAILED.equals(fromState)) {
			// StartFailed -> Stopped
			// StartFailed -> StopFailed
			// StartFailed -> Started
			if (RUNTIME_STATE.STOPPED.equals(toState) || RUNTIME_STATE.STOP_FAILED.equals(toState) || RUNTIME_STATE.STARTED.equals(toState)) {
				return true;
			}
		}

		return false;
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
