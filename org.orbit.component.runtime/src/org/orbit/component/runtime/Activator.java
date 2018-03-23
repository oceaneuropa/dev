package org.orbit.component.runtime;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.runtime.cli.ServicesCommand;
import org.orbit.component.runtime.extensions.Extensions;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static BundleContext context;
	protected static Activator instance;

	public static BundleContext getBundleContext() {
		return context;
	}

	public static Activator getInstance() {
		return instance;
	}

	protected ServicesCommand servicesCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");

		Activator.context = bundleContext;
		Activator.instance = this;

		// Register program extensions
		Extensions.INSTANCE.start(bundleContext);

		// Start commands and services
		this.servicesCommand = new ServicesCommand();
		this.servicesCommand.start(bundleContext);

		// Start tracking web service connector services
		OrbitClients.getInstance().start(bundleContext);

		// Start tracking services for starting web service and indexer
		OrbitServices.getInstance().start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		// Stop tracking services for stopping web service and indexer
		OrbitServices.getInstance().stop(bundleContext);

		// Stop tracking web service connector services
		OrbitClients.getInstance().stop(bundleContext);

		// Stop commands and services
		if (this.servicesCommand != null) {
			this.servicesCommand.stop(bundleContext);
			this.servicesCommand = null;
		}

		// Unregister program extensions
		Extensions.INSTANCE.stop(bundleContext);

		Activator.instance = null;
		Activator.context = null;
	}

}
