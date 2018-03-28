package org.origin.common.service;

import org.osgi.framework.BundleContext;

public abstract class ServiceRegistry<S> {

	/**
	 * 
	 * @param bundleContext
	 */
	public abstract void start(BundleContext bundleContext);

	/**
	 * 
	 * @param bundleContext
	 */
	public abstract void stop(BundleContext bundleContext);

	/**
	 * 
	 * @return
	 */
	public abstract S[] getServices();

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	public abstract void register(BundleContext bundleContext, S service);

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	public abstract void unregister(BundleContext bundleContext, S service);

}
