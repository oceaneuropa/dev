package org.orbit.component.api;

import org.orbit.component.api.util.ComponentClients;
import org.orbit.component.api.util.ComponentsConfigPropertiesHandler;
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
		Activator.context = bundleContext;
		Activator.instance = this;

		ComponentsConfigPropertiesHandler.getInstance().start(bundleContext);

		ComponentClients.getInstance().start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		ComponentClients.getInstance().stop(bundleContext);

		ComponentsConfigPropertiesHandler.getInstance().stop(bundleContext);

		Activator.context = null;
		Activator.instance = null;
	}

}
