package org.origin.common.extensions.core;

import org.origin.common.extensions.core.impl.ExtensionRegistryImpl;
import org.osgi.framework.BundleContext;

public abstract class IExtensionRegistry {

	private static Object lock = new Object[0];
	private static IExtensionRegistry instance = null;

	public static IExtensionRegistry getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new ExtensionRegistryImpl();
				}
			}
		}
		return instance;
	}

	/**
	 * Register a extension.
	 * 
	 * @param context
	 * @param extension
	 */
	public abstract void register(BundleContext context, IExtension extension);

	/**
	 * Unregister a extension.
	 * 
	 * @param context
	 * @param extension
	 */
	public abstract void unregister(BundleContext context, IExtension extension);

}
