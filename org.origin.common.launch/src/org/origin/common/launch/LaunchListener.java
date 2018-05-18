package org.origin.common.launch;

public interface LaunchListener {

	void launchRemoved(LaunchHandler launch);

	void launchAdded(LaunchHandler launch);

	void launchChanged(LaunchHandler launch);

}
