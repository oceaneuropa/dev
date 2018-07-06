package org.orbit.component.runtime.tier3.nodecontrol.util;

import org.origin.common.launch.LaunchActivator;
import org.origin.common.launch.LaunchService;
import org.origin.common.launch.LaunchType;

public class NodeHelper {

	public static NodeHelper INSTANCE = new NodeHelper();

	public LaunchService getLaunchService() {
		LaunchService launchService = LaunchActivator.getDefault().getLaunchService();
		return launchService;
	}

	public String getLaunchTypeId() {
		return LaunchType.Types.NODE.getId();
	}

	public String getLaunchConfigName(String nodeId) {
		String launchConfigName = "node_launch_config_" + nodeId;
		return launchConfigName;
	}

}
