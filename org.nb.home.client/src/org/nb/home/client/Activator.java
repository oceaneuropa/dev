package org.nb.home.client;

import org.nb.home.client.cli.HomeControlCommand;
import org.nb.home.client.cli.HomeLoginCommand;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected HomeLoginCommand homeLoginCommand;
	protected HomeControlCommand homeControlCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// Start HomeLoginCommand
		this.homeLoginCommand = new HomeLoginCommand(bundleContext);
		this.homeLoginCommand.start();

		// Start HomeControlCommand
		this.homeControlCommand = new HomeControlCommand(bundleContext);
		this.homeControlCommand.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;

		// Stop HomeLoginCommand
		if (this.homeLoginCommand != null) {
			this.homeLoginCommand.stop();
			this.homeLoginCommand = null;
		}

		// Stop HomeControlCommand
		if (this.homeControlCommand != null) {
			this.homeControlCommand.stop();
			this.homeControlCommand = null;
		}
	}

}
