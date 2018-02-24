package org.orbit.platform.runtime.processes;

import java.util.ArrayList;
import java.util.List;

import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ProcessImpl;
import org.orbit.platform.sdk.extension.IProgramExtension;
import org.osgi.framework.BundleContext;

public class ProcessManagerImpl implements ProcessManager {

	protected BundleContext bundleContext;
	protected List<ProcessHandler> processHandlers = new ArrayList<ProcessHandler>();

	public ProcessManagerImpl() {
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		this.bundleContext = bundleContext;

		// Start tracking IProgramExtension services of ServiceActivator
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		// Stop tracking IProgramExtension services of ServiceActivator

		this.bundleContext = null;
	}

	@Override
	public List<ProcessHandler> getProcessHandlers() {
		return this.processHandlers;
	}

	@Override
	public List<IProcess> getProcesses() {
		List<IProcess> processes = new ArrayList<IProcess>();
		for (ProcessHandler processHandler : this.processHandlers) {
			IProcess process = processHandler.getProcess();
			processes.add(process);
		}
		return processes;
	}

	@Override
	public List<IProcess> getProcesses(String extensionTypeId) {
		List<IProcess> processes = new ArrayList<IProcess>();
		for (ProcessHandler processHandler : this.processHandlers) {
			String currExtensionTypeId = processHandler.getExtensionTypeId();
			if (extensionTypeId != null && extensionTypeId.equals(currExtensionTypeId)) {
				IProcess process = processHandler.getProcess();
				processes.add(process);
			}
		}
		return processes;
	}

	@Override
	public List<IProcess> getProcesses(String extensionTypeId, String extensionId) {
		List<IProcess> processes = new ArrayList<IProcess>();
		for (ProcessHandler processRecord : this.processHandlers) {
			String currExtensionTypeId = processRecord.getExtensionTypeId();
			String currExtensionId = processRecord.getExtensionId();
			if (extensionTypeId != null && extensionTypeId.equals(currExtensionTypeId) //
					&& extensionId != null && extensionId.equals(currExtensionId)//
			) {
				IProcess process = processRecord.getProcess();
				processes.add(process);
			}
		}
		return processes;
	}

	@Override
	public IProcess getProcess(int pid) {
		IProcess process = null;
		for (ProcessHandler processRecord : this.processHandlers) {
			int currPID = processRecord.getPID();
			if (pid == currPID) {
				process = processRecord.getProcess();
			}
		}
		return process;
	}

	@Override
	public synchronized IProcess createProcess(IProgramExtension extension, String processName) {
		String extensionTypeId = extension.getTypeId();
		String extensionId = extension.getId();

		int pid = getNextPID();

		ProcessImpl process = new ProcessImpl();
		process.setPID(pid);
		process.setName(processName);

		ProcessHandlerImpl processHandler = new ProcessHandlerImpl(this, process);
		processHandler.setExtensionTypeId(extensionTypeId);
		processHandler.setExtensionId(extensionId);
		processHandler.setProcess(process);
		this.processHandlers.add(processHandler);

		return process;
	}

	@Override
	public synchronized boolean removeProcess(IProcess process) {
		if (process != null) {
			int pid = process.getPID();
			ProcessHandler processRecord = getProcessRecord(pid);
			if (processRecord != null) {
				return this.processHandlers.remove(processRecord);
			}
		}
		return false;
	}

	protected synchronized int getNextPID() {
		int pid = 1;
		for (ProcessHandler currProcessRecord : this.processHandlers) {
			int currPID = currProcessRecord.getPID();
			if (pid == currPID) {
				pid += 1;
			}
		}
		return pid;
	}

	protected ProcessHandler getProcessRecord(int pid) {
		ProcessHandler processRecord = null;
		for (ProcessHandler currProcessRecord : this.processHandlers) {
			int currPID = currProcessRecord.getPID();
			if (pid == currPID) {
				processRecord = currProcessRecord;
				break;
			}
		}
		return processRecord;
	}

}
