package org.origin.common.launch;

import java.io.IOException;

public interface LaunchInstance {

	LaunchConfig getLaunchConfiguration();

	String getId();

	void setAttribute(String key, String value);

	String getAttribute(String key);

	boolean canTerminate();

	boolean isTerminated();

	void terminate() throws IOException;

	ProcessInstance[] getProcessInstances();

	boolean addProcessInstance(ProcessInstance processInstance);

	void addProcessInstances(ProcessInstance[] processInstances);

	boolean removeProcessInstance(ProcessInstance processInstance);

}
