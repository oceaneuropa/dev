package org.origin.common.launch;

import org.origin.common.launch.cli.LaunchCommand;
import org.origin.common.launch.impl.LaunchServiceImpl;
import org.origin.common.launch.util.LaunchServiceTracker;
import org.origin.common.osgi.AbstractBundleActivator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class LaunchActivator extends AbstractBundleActivator implements BundleActivator {

	protected static LaunchActivator plugin;
	protected static BundleContext bundleContext;

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public static LaunchActivator getDefault() {
		return plugin;
	}

	protected LaunchServiceTracker launchServiceTracker;
	protected LaunchCommand command;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LaunchActivator.bundleContext = bundleContext;
		LaunchActivator.plugin = this;

		// Start tracking launch service
		this.launchServiceTracker = new LaunchServiceTracker();
		this.launchServiceTracker.start(bundleContext);

		// Start default impl of the launch service
		LaunchServiceImpl.getInstance().start(bundleContext);

		// Start CLI
		LaunchCommand.INSTANCE.start(bundleContext);

		// Register extensions
		Extensions.INSTANCE.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// Unregister extensions
		Extensions.INSTANCE.stop(bundleContext);

		// Stop CLI
		LaunchCommand.INSTANCE.stop(bundleContext);

		// Stop default impl of the launch service
		LaunchServiceImpl.getInstance().stop(bundleContext);

		// Stop tracking launch service
		if (this.launchServiceTracker != null) {
			this.launchServiceTracker.stop(bundleContext);
			this.launchServiceTracker = null;
		}

		LaunchActivator.plugin = null;
		LaunchActivator.bundleContext = null;
	}

	public LaunchService getLaunchService() {
		return (this.launchServiceTracker != null) ? this.launchServiceTracker.getService() : null;
	}

}
