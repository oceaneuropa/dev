package org.orbit.platform.sdk.util;

import org.orbit.platform.sdk.IProcess;

public interface IProcessFilter {

	/**
	 * 
	 * @param process
	 * @return
	 */
	boolean accept(IProcess process);

}
