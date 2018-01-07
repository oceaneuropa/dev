package org.orbit.infra.cli;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected ServicesCommand servicesCommand;
	protected IndexServiceCommand indexServiceCommand;
	protected ChannelCommand channelCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// Start commands
		this.servicesCommand = new ServicesCommand();
		this.servicesCommand.start(bundleContext);

		this.indexServiceCommand = new IndexServiceCommand();
		this.indexServiceCommand.start(bundleContext);

		this.channelCommand = new ChannelCommand();
		this.channelCommand.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// Stop commands
		if (this.servicesCommand != null) {
			this.servicesCommand.stop(bundleContext);
			this.servicesCommand = null;
		}

		if (this.indexServiceCommand != null) {
			this.indexServiceCommand.stop(bundleContext);
			this.indexServiceCommand = null;
		}

		if (this.channelCommand != null) {
			this.channelCommand.stop(bundleContext);
			this.channelCommand = null;
		}

		Activator.context = null;
	}

}
