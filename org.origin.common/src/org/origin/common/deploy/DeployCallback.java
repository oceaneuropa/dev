package org.origin.common.deploy;

/**
 * 
 */
public interface DeployCallback {

	/**
	 * 
	 * @param target
	 */
	public void deployedTo(Object target);

	/**
	 * 
	 * @param target
	 */
	public void undeployedFrom(Object target);

}
