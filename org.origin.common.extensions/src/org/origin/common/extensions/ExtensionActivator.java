package org.origin.common.extensions;

import org.origin.common.extensions.cli.ExtensionCommand;
import org.origin.common.extensions.core.IExtensionService;
import org.origin.common.extensions.core.impl.ExtensionServiceImpl;
import org.origin.common.extensions.util.ExtensionServiceTracker;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ExtensionActivator implements BundleActivator {

	protected static ExtensionActivator plugin;
	protected static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}

	public static ExtensionActivator getDefault() {
		return plugin;
	}

	protected ExtensionServiceTracker extensionServiceTracker;
	protected ExtensionCommand command;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		ExtensionActivator.context = bundleContext;
		ExtensionActivator.plugin = this;

		// Start tracking extension service
		this.extensionServiceTracker = new ExtensionServiceTracker();
		this.extensionServiceTracker.start(bundleContext);

		// Start program extension service impl
		ExtensionServiceImpl.getInstance().start(bundleContext);

		// Start CLI
		this.command = new ExtensionCommand();
		this.command.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// Stop CLI
		if (this.command != null) {
			this.command.stop(bundleContext);
			this.command = null;
		}

		// Stop program extension service impl
		ExtensionServiceImpl.getInstance().stop(bundleContext);

		// Stop tracking extension service
		if (this.extensionServiceTracker != null) {
			this.extensionServiceTracker.stop(bundleContext);
			this.extensionServiceTracker = null;
		}

		ExtensionActivator.plugin = null;
		ExtensionActivator.context = null;
	}

	public IExtensionService getExtensionService() {
		return (this.extensionServiceTracker != null) ? this.extensionServiceTracker.getService() : null;
	}

}
