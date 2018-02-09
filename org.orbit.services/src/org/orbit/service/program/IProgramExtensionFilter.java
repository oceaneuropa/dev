package org.orbit.service.program;

import org.orbit.service.program.IProgramExtension.Context;

public interface IProgramExtensionFilter {

	/**
	 * @param context
	 * @return
	 */
	boolean accept(Context context);

}
