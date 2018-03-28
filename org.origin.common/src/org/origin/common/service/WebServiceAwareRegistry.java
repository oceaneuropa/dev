package org.origin.common.service;

import org.osgi.framework.BundleContext;

public abstract class WebServiceAwareRegistry {

	private static Object lock = new Object[0];
	private static WebServiceAwareRegistry instance = null;

	public static WebServiceAwareRegistry getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new WebServiceAwareRegistryImpl();
				}
			}
		}
		return instance;
	}

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
	public abstract WebServiceAware[] getServices();

	/**
	 * 
	 * @param bundleContext
	 * @param webServiceAware
	 */
	public abstract void register(BundleContext bundleContext, WebServiceAware webServiceAware);

	/**
	 * 
	 * @param bundleContext
	 * @param webServiceAware
	 */
	public abstract void unregister(BundleContext bundleContext, WebServiceAware webServiceAware);

}
