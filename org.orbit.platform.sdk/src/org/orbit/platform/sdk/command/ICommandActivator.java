package org.orbit.platform.sdk.command;

import org.osgi.framework.BundleContext;

public interface ICommandActivator {

	public static final String TYPE_ID = "platform.extension.CommandActivator";

	/**
	 * 
	 * @param bundleContext
	 */
	void start(BundleContext bundleContext);

	/**
	 * 
	 * @param bundleContext
	 */
	void stop(BundleContext bundleContext);

}
