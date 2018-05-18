package org.origin.common.launch.impl;

import java.io.IOException;

import org.origin.common.launch.LaunchConfiguration;
import org.origin.common.launch.LaunchService;
import org.origin.common.launch.LaunchHandler;

public interface LaunchInternalService extends LaunchService {

	boolean launchConfigurationAdded(LaunchConfiguration config) throws IOException;

	boolean launchConfigurationRemoved(LaunchConfiguration config) throws IOException;

	boolean launchAdded(LaunchHandler launch);

	boolean launchRemoved(LaunchHandler launch);

}
