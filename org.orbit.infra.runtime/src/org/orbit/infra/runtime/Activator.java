package org.orbit.infra.runtime;

import org.orbit.infra.runtime.cli.InfraCommand;
import org.orbit.infra.runtime.extension.Extensions;
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

	protected InfraCommand infraCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.bundleContext = bundleContext;
		Activator.instance = this;

		// Register program extensions
		Extensions.INSTANCE.start(bundleContext);

		// Start command and services
		this.infraCommand = new InfraCommand();
		this.infraCommand.start(bundleContext);

		// Start tracking services
		InfraServices.getInstance().start(bundleContext);

		// Start relays
		// InfraRelays.getInstance().start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.instance = null;
		Activator.bundleContext = null;

		// Stop relays
		// InfraRelays.getInstance().stop(bundleContext);

		// Stop tracking services
		InfraServices.getInstance().stop(bundleContext);

		// Stop command and services
		if (this.infraCommand != null) {
			this.infraCommand.stop(bundleContext);
			this.infraCommand = null;
		}

		// Unregister program extensions
		Extensions.INSTANCE.stop(bundleContext);
	}

}
