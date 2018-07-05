package org.origin.common.launch;

import java.io.IOException;

public interface LaunchInternalService extends LaunchService {

	boolean launchConfigurationAdded(LaunchConfig launchConfig) throws IOException;

	boolean launchConfigurationRemoved(LaunchConfig launchConfig) throws IOException;

	boolean launchInstanceAdded(LaunchInstance launchInstance);

	boolean launchInstsanceRemoved(LaunchInstance launchInstance);

}
