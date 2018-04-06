package org.orbit.component.runtime;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.runtime.cli.DomainManagementCommand;
import org.orbit.component.runtime.cli.NodeControlCommand;
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
	protected DomainManagementCommand domainManagementCommand;
	protected NodeControlCommand nodeControlCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");

		Activator.context = bundleContext;
		Activator.instance = this;

		// Register extensions
		Extensions.INSTANCE.start(bundleContext);

		// Start tracking web service connector services
		OrbitClients.getInstance().start(bundleContext);

		// Start tracking services for starting web service and indexer
		OrbitServices.getInstance().start(bundleContext);

		// Start commands
		this.servicesCommand = new ServicesCommand();
		this.servicesCommand.start(bundleContext);

		this.domainManagementCommand = new DomainManagementCommand();
		this.domainManagementCommand.start(bundleContext);

		this.nodeControlCommand = new NodeControlCommand();
		this.nodeControlCommand.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		// Stop commands
		if (this.servicesCommand != null) {
			this.servicesCommand.stop(bundleContext);
			this.servicesCommand = null;
		}

		if (this.domainManagementCommand != null) {
			this.domainManagementCommand.stop(bundleContext);
			this.domainManagementCommand = null;
		}

		if (this.nodeControlCommand != null) {
			this.nodeControlCommand.stop(bundleContext);
			this.nodeControlCommand = null;
		}

		// Stop tracking services for stopping web service and indexer
		OrbitServices.getInstance().stop(bundleContext);

		// Stop tracking web service connector services
		OrbitClients.getInstance().stop(bundleContext);

		// Unregister extensions
		Extensions.INSTANCE.stop(bundleContext);

		Activator.instance = null;
		Activator.context = null;
	}

}
