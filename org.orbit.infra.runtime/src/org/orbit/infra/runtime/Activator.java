package org.orbit.infra.runtime;

import org.orbit.infra.runtime.util.ConfigRegistryConfigPropertiesHandler;
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

		// Load config properties
		ConfigRegistryConfigPropertiesHandler.getInstance().start(bundleContext);

		// Start tracking infra services
		InfraServices.getInstance().start(bundleContext);

		// Register extensions
		Extensions.INSTANCE.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("stop");

		// Unregister extensions
		Extensions.INSTANCE.stop(bundleContext);

		// Stop tracking infra services
		InfraServices.getInstance().stop(bundleContext);

		// Dispose config properties
		ConfigRegistryConfigPropertiesHandler.getInstance().stop(bundleContext);

		Activator.instance = null;
		Activator.bundleContext = null;
	}

}

// protected InfraCommand infraCommand;

// Start command and services
// this.infraCommand = new InfraCommand();
// this.infraCommand.start(bundleContext);

// Stop command and services
// if (this.infraCommand != null) {
// this.infraCommand.stop(bundleContext);
// this.infraCommand = null;
// }

// Start relays
// InfraRelays.getInstance().start(bundleContext);

// Stop relays
// InfraRelays.getInstance().stop(bundleContext);
