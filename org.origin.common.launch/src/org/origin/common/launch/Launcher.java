package org.origin.common.launch;

import java.io.IOException;

public interface Launcher {

	public static final String TYPE_ID = "org.origin.common.launch.Launcher";

	public static final String PROP_TYPE_ID = "typeId";

	/**
	 * 
	 * @param config
	 * @param launch
	 * @throws IOException
	 */
	void launch(LaunchConfiguration config, LaunchHandler launch) throws IOException;

}
