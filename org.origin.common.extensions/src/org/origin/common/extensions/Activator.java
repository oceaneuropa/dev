package org.origin.common.extensions;

import org.origin.common.extensions.core.IExtensionService;
import org.origin.common.extensions.util.ExtensionServiceTracker;
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

	protected ExtensionServiceTracker extensionServiceTracker;

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.plugin = this;

		// Start tracking program extension service
		this.extensionServiceTracker = new ExtensionServiceTracker();
		this.extensionServiceTracker.start(bundleContext);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		// Stop tracking program extension service
		if (this.extensionServiceTracker != null) {
			this.extensionServiceTracker.stop(bundleContext);
			this.extensionServiceTracker = null;
		}

		Activator.plugin = null;
		Activator.context = null;
	}

	public IExtensionService getExtensionService() {
		return (this.extensionServiceTracker != null) ? this.extensionServiceTracker.getService() : null;
	}

}
