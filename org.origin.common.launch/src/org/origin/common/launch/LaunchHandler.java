package org.origin.common.launch;

import java.io.IOException;

public interface LaunchHandler {

	String getId();

	LaunchConfiguration getLaunchConfiguration();

	void setAttribute(String key, String value);

	String getAttribute(String key);

	ProcessHandler[] getProcesses();

	boolean addProcess(ProcessHandler process);

	void addProcesses(ProcessHandler[] processes);

	boolean removeProcess(ProcessHandler process);

	boolean canTerminate();

	boolean isTerminated();

	void terminate() throws IOException;

}
