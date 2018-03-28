package org.origin.common.service;

import org.osgi.framework.BundleContext;

public abstract class CLIAwareRegistry {

	private static Object lock = new Object[0];
	private static CLIAwareRegistry instance = null;

	public static CLIAwareRegistry getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new CLIAwareRegistryImpl();
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
	public abstract CLIAware[] getServices();

	/**
	 * 
	 * @param bundleContext
	 * @param cliAware
	 */
	public abstract void register(BundleContext bundleContext, CLIAware cliAware);

	/**
	 * 
	 * @param bundleContext
	 * @param cliAware
	 */
	public abstract void unregister(BundleContext bundleContext, CLIAware cliAware);

}
