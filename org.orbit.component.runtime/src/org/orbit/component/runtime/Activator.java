package org.orbit.component.runtime;

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

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.debug("start()");

		Activator.context = bundleContext;
		Activator.instance = this;

		// Register extensions
		Extensions.INSTANCE.start(bundleContext);

		// Start service adapters
		OrbitServices.getInstance().start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("stop()");

		// Stop service adapters
		OrbitServices.getInstance().stop(bundleContext);

		// Unregister extensions
		Extensions.INSTANCE.stop(bundleContext);

		Activator.instance = null;
		Activator.context = null;
	}

}

// protected ServicesCommand servicesCommand;
// protected DomainManagementCommand domainManagementCommand;
// protected NodeControlCommand nodeControlCommand;

// Start commands
// this.servicesCommand = new ServicesCommand();
// this.servicesCommand.start(bundleContext);
//
// this.domainManagementCommand = new DomainManagementCommand();
// this.domainManagementCommand.start(bundleContext);
//
// this.nodeControlCommand = new NodeControlCommand();
// this.nodeControlCommand.start(bundleContext);

// Stop commands
// if (this.servicesCommand != null) {
// this.servicesCommand.stop(bundleContext);
// this.servicesCommand = null;
// }
//
// if (this.domainManagementCommand != null) {
// this.domainManagementCommand.stop(bundleContext);
// this.domainManagementCommand = null;
// }
//
// if (this.nodeControlCommand != null) {
// this.nodeControlCommand.stop(bundleContext);
// this.nodeControlCommand = null;
// }

// import org.orbit.component.api.OrbitClients;

// Start tracking web service connector services
// OrbitClients.getInstance().start(bundleContext);

// Stop tracking web service connector services
// OrbitClients.getInstance().stop(bundleContext);
