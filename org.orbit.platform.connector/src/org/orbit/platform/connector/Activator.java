package org.orbit.platform.connector;

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

// Connectors.getInstance().start(bundleContext);
// Connectors.getInstance().stop(bundleContext);
