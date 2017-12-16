package org.origin.common.deploy;

public interface DeployCallback<T> {

	/**
	 * 
	 * @param target
	 */
	public void deployedTo(T target);

	/**
	 * 
	 * @param target
	 */
	public void undeployedFrom(T target);

}
