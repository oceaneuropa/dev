package org.origin.common.launch.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.origin.common.launch.LaunchConfig;
import org.origin.common.launch.LaunchConstants;
import org.origin.common.launch.LaunchInstance;
import org.origin.common.launch.LaunchInternalService;
import org.origin.common.launch.LaunchService;
import org.origin.common.launch.ProcessInstance;

public class LaunchInstanceImpl implements LaunchInstance {

	protected LaunchConfig launchConfig;
	protected HashMap<String, String> attributes;
	protected List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();

	/**
	 * 
	 * @param launchConfig
	 */
	public LaunchInstanceImpl(LaunchConfig launchConfig) {
		this.launchConfig = launchConfig;
	}

	@Override
	public LaunchConfig getLaunchConfiguration() {
		return this.launchConfig;
	}

	@Override
	public String getId() {
		String id = getAttribute(LaunchConstants.LAUNCH_INSTANCE_ID);
		return id;
	}

	@Override
	public void setAttribute(String key, String value) {
		if (this.attributes == null) {
			this.attributes = new HashMap<String, String>(5);
		}
		this.attributes.put(key, value);
	}

	@Override
	public String getAttribute(String key) {
		if (this.attributes == null) {
			return null;
		}
		return this.attributes.get(key);
	}

	@Override
	public boolean canTerminate() {
		for (ProcessInstance currProcessInstance : this.processInstances) {
			if (currProcessInstance.canTerminate()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isTerminated() {
		for (ProcessInstance currProcessInstance : this.processInstances) {
			if (!currProcessInstance.isTerminated()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void terminate() throws IOException {
		ProcessInstance[] processInstances = getProcessInstances();
		for (int i = 0; i < processInstances.length; i++) {
			ProcessInstance currProcessInstance = processInstances[i];
			if (currProcessInstance.canTerminate()) {
				currProcessInstance.terminate();
			}
		}

		LaunchService launchService = getLaunchConfiguration().getLaunchService();
		if (launchService instanceof LaunchInternalService) {
			((LaunchInternalService) launchService).launchInstsanceRemoved(this);
		}
	}

	@Override
	public ProcessInstance[] getProcessInstances() {
		return this.processInstances.toArray(new ProcessInstance[this.processInstances.size()]);
	}

	@Override
	public boolean addProcessInstance(ProcessInstance processInstance) {
		boolean succeed = false;
		if (processInstance != null && !this.processInstances.contains(processInstance)) {
			succeed = this.processInstances.add(processInstance);
		}
		return succeed;
	}

	@Override
	public void addProcessInstances(ProcessInstance[] processInstsances) {
		if (processInstsances != null) {
			for (ProcessInstance currProcessInstance : processInstsances) {
				addProcessInstance(currProcessInstance);
			}
		}
	}

	@Override
	public boolean removeProcessInstance(ProcessInstance processInstance) {
		boolean succeed = false;
		if (processInstance != null && this.processInstances.contains(processInstance)) {
			succeed = this.processInstances.remove(processInstance);
		}
		return succeed;
	}

}
