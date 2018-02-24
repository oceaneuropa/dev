package org.orbit.platform.runtime.processes;

import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ProcessImpl;

public class ProcessHandlerImpl implements ProcessHandler {

	protected ProcessManager processManagementService;
	protected IProcess process;
	protected String extensionTypeId;
	protected String extensionId;

	/**
	 * 
	 * @param processManagementService
	 * @param process
	 */
	public ProcessHandlerImpl(ProcessManager processManagementService, ProcessImpl process) {
		this.processManagementService = processManagementService;
		this.process = process;
	}

	public int getPID() {
		return this.process.getPID();
	}

	public String getExtensionTypeId() {
		return this.extensionTypeId;
	}

	public void setExtensionTypeId(String extensionTypeId) {
		this.extensionTypeId = extensionTypeId;
	}

	public String getExtensionId() {
		return this.extensionId;
	}

	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
	}

	public IProcess getProcess() {
		return this.process;
	}

	public void setProcess(IProcess process) {
		this.process = process;
	}

}

// @Override
// public void stop() {
// Platform platform = this.getAdapter(Platform.class);
// if (platform != null) {
// platform.stop(this);
// }
// }
