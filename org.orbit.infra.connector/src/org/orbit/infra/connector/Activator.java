package org.orbit.infra.connector;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static BundleContext bundleContext;
	protected static Activator instance;

	public static BundleContext getContext() {
		return bundleContext;
	}

	public static Activator getInstance() {
		return instance;
	}

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.debug("start()");

		Activator.bundleContext = bundleContext;
		Activator.instance = this;

		// Register extensions
		Extensions.INSTANCE.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("stop()");

		// Unregister extensions
		Extensions.INSTANCE.stop(bundleContext);

		Activator.instance = null;
		Activator.bundleContext = null;
	}

}

// protected ServicesCommand servicesCommand;
// protected IndexServiceCommand indexServiceCommand;
// protected ChannelCommand channelCommand;

// Start connector services
// Connectors.getInstance().start(bundleContext);

// Stop connector services
// Connectors.getInstance().stop(bundleContext);

// Start commands
// this.servicesCommand = new ServicesCommand();
// this.servicesCommand.start(bundleContext);
//
// this.indexServiceCommand = new IndexServiceCommand();
// this.indexServiceCommand.start(bundleContext);
//
// this.channelCommand = new ChannelCommand();
// this.channelCommand.start(bundleContext);

// Stop commands
// if (this.servicesCommand != null) {
// this.servicesCommand.stop(bundleContext);
// this.servicesCommand = null;
// }
//
// if (this.indexServiceCommand != null) {
// this.indexServiceCommand.stop(bundleContext);
// this.indexServiceCommand = null;
// }
//
// if (this.channelCommand != null) {
// this.channelCommand.stop(bundleContext);
// this.channelCommand = null;
// }
