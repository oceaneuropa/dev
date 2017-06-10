package org.origin.common.osgi;

public interface DependencyListener {

	/**
	 * 
	 * @param event
	 */
	void onDependencyChange(DependencyEvent event);

}
