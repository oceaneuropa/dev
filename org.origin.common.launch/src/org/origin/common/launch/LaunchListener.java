package org.origin.common.launch;

public interface LaunchListener {

	void launchRemoved(LaunchInstance launch);

	void launchAdded(LaunchInstance launch);

	void launchChanged(LaunchInstance launch);

}
