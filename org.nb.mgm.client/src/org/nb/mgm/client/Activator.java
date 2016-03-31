package org.nb.mgm.client;

import org.nb.mgm.cli.felix.ManagementCommand;
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

		this.mgmCommand = new ManagementCommand(bundleContext);
		this.mgmCommand.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;

		if (this.mgmCommand != null) {
			this.mgmCommand.stop();
		}
	}

}
