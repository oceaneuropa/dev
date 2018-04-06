package org.orbit.platform.sdk.extensions;

import java.nio.file.Path;

public interface LaunchConfiguration {

	String getTypeId();

	String getName();

	void setName(String name);

	Path getPath();

	void setPath(Path path);

}
