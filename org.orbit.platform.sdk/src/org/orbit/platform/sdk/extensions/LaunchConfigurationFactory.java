package org.orbit.platform.sdk.extensions;

import java.nio.file.Path;

public interface LaunchConfigurationFactory {

	public static final String TYPE_ID = "platform.sdk.LaunchConfiguration";

	LaunchConfiguration createLaunchConfiguration(Path path);

	Launcher getLauncher();

}
