package org.origin.common.osgi;

public interface DependencySetListener {

	/**
	 * 
	 * @param event
	 */
	void onDependencySetChange(DependencySetEvent event);

}
