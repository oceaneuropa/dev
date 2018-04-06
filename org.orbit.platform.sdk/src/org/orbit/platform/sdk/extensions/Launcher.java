package org.orbit.platform.sdk.extensions;

import java.io.IOException;

public interface Launcher {

	Object launch(LaunchConfiguration config) throws IOException;

}
