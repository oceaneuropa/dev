package org.orbit.platform.sdk.other;

import org.origin.common.extensions.core.IExtension.Context;

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
