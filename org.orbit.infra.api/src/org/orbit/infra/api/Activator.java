package org.orbit.infra.api;

import org.orbit.infra.api.util.ExtensionsRegister;
import org.orbit.infra.api.util.InfraClients;
import org.orbit.infra.api.util.InfraConfigPropertiesHandler;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static Activator plugin;

	public static Activator getDefault() {
		return plugin;
	}

	public static BundleContext getContext() {
		return context;
	}

	protected ExtensionsRegister extensionsRegister;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.plugin = this;
		Activator.context = bundleContext;

		InfraConfigPropertiesHandler.getInstance().start(bundleContext);

		InfraClients.getInstance().start(bundleContext);

		this.extensionsRegister = new ExtensionsRegister();
		this.extensionsRegister.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.extensionsRegister != null) {
			this.extensionsRegister.stop(bundleContext);
		}

		InfraClients.getInstance().stop(bundleContext);

		InfraConfigPropertiesHandler.getInstance().stop(bundleContext);

		Activator.context = null;
		Activator.plugin = null;
	}

}
