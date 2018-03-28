package org.origin.common.service;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class CLIAwareRegistryImpl extends CLIAwareRegistry {

	protected Map<CLIAware, ServiceRegistration<?>> serviceToRegistrationMap = new HashMap<CLIAware, ServiceRegistration<?>>();
	protected CLIAwareTracker tracker;

	@Override
	public void start(BundleContext bundleContext) {
		this.tracker = new CLIAwareTracker();
		this.tracker.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.tracker != null) {
			this.tracker.stop(bundleContext);
			this.tracker = null;
		}
	}

	@Override
	public CLIAware[] getServices() {
		CLIAware[] services = null;
		if (this.tracker != null) {
			services = this.tracker.getServices();
		}
		if (services == null) {
			services = CLIAware.EMPTY_ARRAY;
		}
		return services;
	}

	@Override
	public void register(BundleContext bundleContext, CLIAware cliAware) {
		if (cliAware == null || this.serviceToRegistrationMap.containsKey(cliAware)) {
			return;
		}

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(CLIAware.PROP_NAME, cliAware.getName() != null ? cliAware.getName() : "n/a");
		ServiceRegistration<?> serviceRegistration = bundleContext.registerService(CLIAware.class, cliAware, props);
		this.serviceToRegistrationMap.put(cliAware, serviceRegistration);
	}

	@Override
	public void unregister(BundleContext bundleContext, CLIAware cliAware) {
		if (cliAware == null) {
			return;
		}
		ServiceRegistration<?> serviceRegistration = this.serviceToRegistrationMap.remove(cliAware);
		if (serviceRegistration != null) {
			serviceRegistration.unregister();
		}
	}

}
