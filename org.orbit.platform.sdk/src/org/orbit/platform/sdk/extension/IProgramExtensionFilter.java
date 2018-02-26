package org.orbit.platform.sdk.extension;

import org.orbit.platform.sdk.extension.IProgramExtension.Context;

public interface IProgramExtensionFilter {

	/**
	 * @param context
	 * @return
	 */
	boolean accept(Context context);

}
