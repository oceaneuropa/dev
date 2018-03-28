package org.origin.common.service;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class CLIAwareTracker {

	protected ServiceTracker<CLIAware, CLIAware> serviceTracker;
	protected List<CLIAware> services = new ArrayList<CLIAware>();

	public CLIAware[] getServices() {
		return this.services.toArray(new CLIAware[this.services.size()]);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		// clean up data
		this.services.clear();

		// Start tracking CLIAware services
		this.serviceTracker = new ServiceTracker<CLIAware, CLIAware>(bundleContext, CLIAware.class, new ServiceTrackerCustomizer<CLIAware, CLIAware>() {
			@Override
			public CLIAware addingService(ServiceReference<CLIAware> reference) {
				CLIAware service = bundleContext.getService(reference);
				if (service != null) {
					serviceAdded(bundleContext, reference, service);
				}
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<CLIAware> reference, CLIAware cliAware) {
			}

			@Override
			public void removedService(ServiceReference<CLIAware> reference, CLIAware cliAware) {
				serviceRemoved(bundleContext, reference, cliAware);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		// Stop tracking CLIAware services
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}

		// clean up data
		this.services.clear();
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 * @param cliAware
	 */
	protected void serviceAdded(BundleContext context, ServiceReference<CLIAware> reference, CLIAware cliAware) {
		String name = (String) reference.getProperty(CLIAware.PROP_NAME);
		if (name != null) {
			if (!this.services.contains(cliAware)) {
				this.services.add(cliAware);
			}
		}
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 * @param cliAware
	 */
	protected void serviceRemoved(BundleContext context, ServiceReference<CLIAware> reference, CLIAware cliAware) {
		String name = (String) reference.getProperty(CLIAware.PROP_NAME);
		if (name != null) {
			if (this.services.contains(cliAware)) {
				this.services.remove(cliAware);
			}
		}
	}

}
