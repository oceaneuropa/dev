package org.orbit.platform.sdk.connector;

import org.osgi.framework.BundleContext;

public interface ConnectorActivator {

	public static final String TYPE_ID = "platform.extension.ConnectorActivator";

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
