package org.orbit.service.program.util;

import org.orbit.service.program.IProgramExtension;
import org.orbit.service.program.impl.ProgramExtensionRegistryImpl;
import org.osgi.framework.BundleContext;

public abstract class ProgramExtensionRegistry {

	private static Object lock = new Object[0];
	private static ProgramExtensionRegistry instance = null;

	public static ProgramExtensionRegistry getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new ProgramExtensionRegistryImpl();
				}
			}
		}
		return instance;
	}

	/**
	 * Register a program extension.
	 * 
	 * @param context
	 * @param programExtension
	 */
	public abstract void register(BundleContext context, IProgramExtension programExtension);

	/**
	 * Unregister a program extension.
	 * 
	 * @param context
	 * @param programExtension
	 */
	public abstract void unregister(BundleContext context, IProgramExtension programExtension);

}
