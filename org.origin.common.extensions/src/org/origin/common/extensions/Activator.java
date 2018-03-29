package org.origin.common.extensions;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	protected static Activator plugin;
	protected static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}

	public static Activator getDefault() {
		return plugin;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.plugin = this;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.plugin = null;
		Activator.context = null;
	}

}
