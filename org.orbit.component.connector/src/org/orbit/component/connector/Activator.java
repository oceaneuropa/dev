package org.orbit.component.connector;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static BundleContext context;
	protected static Activator instance;

	public static BundleContext getContext() {
		return context;
	}

	public static Activator getInstance() {
		return instance;
	}

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		LOG.debug("start()");

		Activator.context = bundleContext;
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
		Activator.context = null;
	}

}

// OrbitConnectors.getInstance().start(bundleContext);
// OrbitConnectors.getInstance().stop(bundleContext);

// OrbitCLIV1.getInstance().start(bundleContext);
// OrbitCLIV1.getInstance().stop(bundleContext);
