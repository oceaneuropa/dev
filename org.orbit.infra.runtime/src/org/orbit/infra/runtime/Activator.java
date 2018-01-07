package org.orbit.infra.runtime;

import org.orbit.infra.runtime.cli.ServicesCommand;
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

	protected ServicesCommand servicesCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.bundleContext = bundleContext;
		Activator.instance = this;

		// Start command and services
		this.servicesCommand = new ServicesCommand();
		this.servicesCommand.start(bundleContext);

		// Start tracking services for starting web service and indexer
		InfraServices.getInstance().start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.instance = null;
		Activator.bundleContext = null;

		// Stop tracking services for stopping web service and indexer
		InfraServices.getInstance().stop(bundleContext);

		// Stop command and services
		if (this.servicesCommand != null) {
			this.servicesCommand.stop(bundleContext);
			this.servicesCommand = null;
		}
	}

}
