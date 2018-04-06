package org.orbit.platform.runtime.extension.launch;

import java.nio.file.Path;

import org.orbit.platform.sdk.extensions.LaunchConfiguration;

public class PlatformLaunchConfiguration implements LaunchConfiguration {

	protected String name;
	protected Path path;

	public PlatformLaunchConfiguration() {
	}

	public PlatformLaunchConfiguration(String name) {
		this.name = name;
	}

	@Override
	public String getTypeId() {
		return "PlatformLaunchConfiguration";
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Path getPath() {
		return this.path;
	}

	@Override
	public void setPath(Path path) {
		this.path = path;
	}

}
