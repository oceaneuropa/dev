package org.orbit.component.api.tier1.auth;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthConnectorAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(AuthConnectorAdapter.class);

	protected BundleContext bundleContext;
	protected ServiceTracker<AuthConnector, AuthConnector> serviceTracker;

	public AuthConnectorAdapter() {
	}

	public AuthConnector getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking AuthConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<AuthConnector, AuthConnector>(bundleContext, AuthConnector.class, new ServiceTrackerCustomizer<AuthConnector, AuthConnector>() {
			@Override
			public AuthConnector addingService(ServiceReference<AuthConnector> reference) {
				AuthConnector connector = bundleContext.getService(reference);
				LOG.info("addingService() AuthConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<AuthConnector> reference, AuthConnector connector) {
				LOG.info("removedService() AuthConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<AuthConnector> reference, AuthConnector connector) {
				LOG.info("removedService() AuthConnector is removed: " + connector);
				connectorRemoved(connector);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking AuthConnector service.
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	public void connectorAdded(AuthConnector connector) {

	}

	public void connectorRemoved(AuthConnector connector) {

	}

}
