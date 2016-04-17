package org.nb.mgm.client;

import org.nb.mgm.client.cli.felix.ManagementCommand;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	protected ManagementCommand mgmCommand;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// Start the OSGi command for management
		this.mgmCommand = new ManagementCommand(bundleContext);
		this.mgmCommand.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;

		// Stop the OSGi command for management
		if (this.mgmCommand != null) {
			this.mgmCommand.stop();
		}
	}

}
