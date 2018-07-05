package org.origin.common.launch;

import java.io.IOException;

public interface LaunchService {

	LaunchType[] getLaunchTypes();

	LaunchType getLaunchType(String typeId);

	Launcher[] getLaunchers();

	Launcher[] getLaunchers(String typeId);

	LaunchConfig[] getLaunchConfigurations() throws IOException;

	LaunchConfig[] getLaunchConfigurations(String typeId) throws IOException;

	LaunchConfig getLaunchConfiguration(String typeId, String name) throws IOException;

	LaunchConfig createLaunchConfiguration(String typeId, String name) throws IOException;

	boolean launchConfigurationExists(String name);

	LaunchInstance[] getLaunchInstances();

	LaunchInstance getLaunchInstance(String id);

}
