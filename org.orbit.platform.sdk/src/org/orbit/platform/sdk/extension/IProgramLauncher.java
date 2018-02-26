package org.orbit.platform.sdk.extension;

import org.orbit.platform.sdk.extension.IProgramExtension.Context;

public interface IProgramLauncher {

	/**
	 * Launch a program.
	 * 
	 * @param context
	 * @return
	 */
	Object launch(Context context);

	/**
	 * Exit the program.
	 * 
	 * @param context
	 * @param instance
	 * @return
	 */
	int exit(Context context, Object instance);

}
