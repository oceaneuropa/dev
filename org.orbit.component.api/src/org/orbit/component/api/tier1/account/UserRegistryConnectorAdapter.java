package org.orbit.component.api.tier1.account;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistryConnectorAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(UserRegistryConnectorAdapter.class);

	protected BundleContext bundleContext;
	protected ServiceTracker<UserRegistryConnector, UserRegistryConnector> serviceTracker;

	public UserRegistryConnectorAdapter() {
	}

	public UserRegistryConnector getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking UserRegistryConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<UserRegistryConnector, UserRegistryConnector>(bundleContext, UserRegistryConnector.class, new ServiceTrackerCustomizer<UserRegistryConnector, UserRegistryConnector>() {
			@Override
			public UserRegistryConnector addingService(ServiceReference<UserRegistryConnector> reference) {
				UserRegistryConnector connector = bundleContext.getService(reference);
				LOG.info("addingService() UserRegistryConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<UserRegistryConnector> reference, UserRegistryConnector connector) {
				LOG.info("removedService() UserRegistryConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<UserRegistryConnector> reference, UserRegistryConnector connector) {
				LOG.info("removedService() UserRegistryConnector is removed: " + connector);
				connectorRemoved(connector);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking UserRegistryConnector service.
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	public void connectorAdded(UserRegistryConnector connector) {

	}

	public void connectorRemoved(UserRegistryConnector connector) {

	}

}
