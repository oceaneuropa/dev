package org.origin.common.launch;

import java.io.IOException;

public interface LaunchService {

	LaunchType[] getLaunchTypes();

	LaunchType getLaunchType(String typeId);

	Launcher[] getLaunchers();

	Launcher[] getLaunchers(String typeId);

	LaunchConfiguration[] getLaunchConfigurations() throws IOException;

	LaunchConfiguration[] getLaunchConfigurations(String typeId) throws IOException;

	LaunchConfiguration getLaunchConfiguration(String typeId, String name) throws IOException;

	LaunchConfiguration createLaunchConfiguration(String typeId, String name) throws IOException;

	boolean launchConfigurationExists(String name);

	LaunchHandler[] getLaunchHandlers();

	LaunchHandler getLaunchHandler(String id);

	ProcessHandler[] getProcessHandlers();

	String[] getEnvironment(LaunchConfiguration configuration) throws IOException;

}

// void addLaunchListener(ILaunchListener listener);

// void removeLaunchListener(ILaunchListener listener);
