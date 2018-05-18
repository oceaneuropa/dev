package org.orbit.platform.runtime.extension.launch.other;

import java.nio.file.Path;

import org.origin.common.launch.LaunchType;

public class PlatformLaunchType implements LaunchType {

	public static String ID = "org.orbit.platform.runtime.PlatformLaunchType";

	protected String name;
	protected Path path;

	public PlatformLaunchType() {
	}

	public PlatformLaunchType(String name) {
		this.name = name;
	}

	@Override
	public String getId() {
		return "PlatformLaunchConfiguration";
	}

	@Override
	public String getName() {
		return this.name;
	}

	// @Override
	// public String[] getLauncherIds() {
	// return null;
	// }

}

// @Override
// public Path getPath() {
// return this.path;
// }
//
// @Override
// public void setPath(Path path) {
// this.path = path;
// }

// @Override
// public String getLauncherId() {
// return null;
// }

// @Override
// public Map<String, Object> getAttributes() {
// return null;
// }
