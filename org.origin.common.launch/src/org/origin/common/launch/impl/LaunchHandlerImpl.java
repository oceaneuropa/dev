package org.origin.common.launch.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.origin.common.launch.LaunchConfiguration;
import org.origin.common.launch.LaunchConstants;
import org.origin.common.launch.LaunchHandler;
import org.origin.common.launch.LaunchService;
import org.origin.common.launch.ProcessHandler;

public class LaunchHandlerImpl implements LaunchHandler {

	protected LaunchConfiguration launchConfig;
	protected HashMap<String, String> attributes;
	protected List<ProcessHandler> processes = new ArrayList<ProcessHandler>();

	/**
	 * 
	 * @param launchConfig
	 */
	public LaunchHandlerImpl(LaunchConfiguration launchConfig) {
		this.launchConfig = launchConfig;
	}

	@Override
	public String getId() {
		String runtimeLaunchId = getAttribute(LaunchConstants.RUNTIME_LAUNCH_ID);
		return runtimeLaunchId;
	}

	@Override
	public LaunchConfiguration getLaunchConfiguration() {
		return this.launchConfig;
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
	public ProcessHandler[] getProcesses() {
		return this.processes.toArray(new ProcessHandler[this.processes.size()]);
	}

	@Override
	public boolean addProcess(ProcessHandler process) {
		boolean succeed = false;
		if (process != null && !this.processes.contains(process)) {
			succeed = this.processes.add(process);
		}
		return succeed;
	}

	@Override
	public void addProcesses(ProcessHandler[] processes) {
		if (processes != null) {
			for (ProcessHandler currProcess : processes) {
				addProcess(currProcess);
			}
		}
	}

	@Override
	public boolean removeProcess(ProcessHandler process) {
		boolean succeed = false;
		if (process != null && this.processes.contains(process)) {
			succeed = this.processes.remove(process);
		}
		return succeed;
	}

	@Override
	public boolean canTerminate() {
		for (ProcessHandler currProcess : this.processes) {
			if (currProcess.canTerminate()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isTerminated() {
		for (ProcessHandler currProcess : this.processes) {
			if (!currProcess.isTerminated()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void terminate() throws IOException {
		ProcessHandler[] processes = getProcesses();
		for (int i = 0; i < processes.length; i++) {
			ProcessHandler process = processes[i];
			if (process.canTerminate()) {
				process.terminate();
			}
		}

		LaunchService launchService = this.getLaunchConfiguration().getLaunchService();
		if (launchService instanceof LaunchInternalService) {
			((LaunchInternalService) launchService).launchRemoved(this);
		}
	}

}
