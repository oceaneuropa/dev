package org.orbit.platform.sdk;

public interface IProcessFilter {

	/**
	 * 
	 * @param process
	 * @return
	 */
	boolean accept(IProcess process);

}
