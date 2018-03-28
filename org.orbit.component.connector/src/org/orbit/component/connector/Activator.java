package org.orbit.component.connector;

import org.orbit.component.cli.OrbitCLI;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

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
		Activator.context = bundleContext;
		Activator.instance = this;

		OrbitConnectors.getInstance().start(bundleContext);
		OrbitCLI.getInstance().start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		OrbitCLI.getInstance().stop(bundleContext);
		OrbitConnectors.getInstance().stop(bundleContext);

		Activator.instance = null;
		Activator.context = null;
	}

}
