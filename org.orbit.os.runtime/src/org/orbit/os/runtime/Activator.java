package org.orbit.os.runtime;

import org.orbit.os.runtime.cli.OSCommand;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static BundleContext bundleContext;
	protected static Activator instance;

	static BundleContext getContext() {
		return bundleContext;
	}

	public static Activator getInstance() {
		return instance;
	}

	protected OSCommand OSCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");

		Activator.bundleContext = bundleContext;
		Activator.instance = this;

		// Start command and services
		this.OSCommand = new OSCommand();
		this.OSCommand.start(bundleContext);
		this.OSCommand.startGAIA();

		// Start tracking services for starting web service and indexer
		OSServices.getInstance().start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		// Stop tracking services for stopping web service and indexer
		OSServices.getInstance().stop(bundleContext);

		// Stop command and services
		if (this.OSCommand != null) {
			this.OSCommand.stopGAIA();
			this.OSCommand.stop(bundleContext);
			this.OSCommand = null;
		}

		Activator.instance = null;
		Activator.bundleContext = null;
	}

}

// SetupUtil.loadNodeConfigIniProperties(bundleContext, indexProviderProps);
