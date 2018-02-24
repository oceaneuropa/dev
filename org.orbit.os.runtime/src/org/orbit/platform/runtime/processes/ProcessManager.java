package org.orbit.platform.runtime.processes;

import java.util.List;

import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.extension.IProgramExtension;

public interface ProcessManager {

	List<ProcessHandler> getProcessHandlers();

	List<IProcess> getProcesses();

	List<IProcess> getProcesses(String extensionTypeId);

	List<IProcess> getProcesses(String extensionTypeId, String extensionId);

	IProcess getProcess(int pid);

	IProcess createProcess(IProgramExtension extension, String processName);

	boolean removeProcess(IProcess process);

}
