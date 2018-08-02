package org.orbit.spirit.connector;

import org.orbit.spirit.cli.GAIAClientCommand;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static BundleContext bundleContext;
	protected static Activator instance;

	public static BundleContext getContext() {
		return bundleContext;
	}

	public static Activator getInstance() {
		return instance;
	}

	protected GAIAClientCommand gaiaCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.debug("start()");

		Activator.bundleContext = bundleContext;
		Activator.instance = this;

		// Register extensions
		Extensions.INSTANCE.start(bundleContext);

		this.gaiaCommand = new GAIAClientCommand();
		this.gaiaCommand.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("stop()");

		if (this.gaiaCommand != null) {
			this.gaiaCommand.stop(bundleContext);
			this.gaiaCommand = null;
		}

		// Unregister extensions
		Extensions.INSTANCE.stop(bundleContext);

		Activator.instance = null;
		Activator.bundleContext = null;
	}

}

// Connectors.getInstance().start(bundleContext);
// Connectors.getInstance().stop(bundleContext);
