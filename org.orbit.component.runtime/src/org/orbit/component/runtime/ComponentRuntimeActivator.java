package org.orbit.component.runtime;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ComponentRuntimeActivator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(ComponentRuntimeActivator.class);

	protected Extensions extensions;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.debug("start()");

		// Register extensions
		this.extensions = new Extensions();
		this.extensions.start(bundleContext);

		// Start service adapters
		ComponentServices.getInstance().start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("stop()");

		// Stop service adapters
		ComponentServices.getInstance().stop(bundleContext);

		// Unregister extensions
		if (this.extensions != null) {
			this.extensions.stop(bundleContext);
			this.extensions = null;
		}
	}

}

// protected static BundleContext context;
// public static BundleContext getBundleContext() {
// return context;
// }

// Activator.context = bundleContext;
// Activator.instance = this;

// Activator.instance = null;
// Activator.context = null;

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
