package org.origin.common.launch;

import java.io.IOException;

public interface Launcher {

	// type of extension
	public static final String TYPE_ID = "org.origin.common.launch.Launcher";

	// property name
	public static final String PROP_TYPE_ID = "typeId";

	/**
	 * 
	 * @param launchConfig
	 * @param launchInstance
	 * @throws IOException
	 */
	void launch(LaunchConfig launchConfig, LaunchInstance launchInstance) throws IOException;

}
