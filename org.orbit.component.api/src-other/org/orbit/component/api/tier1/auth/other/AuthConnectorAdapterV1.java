package org.orbit.component.api.tier1.auth.other;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthConnectorAdapterV1 {

	protected static Logger LOG = LoggerFactory.getLogger(AuthConnectorAdapterV1.class);

	protected BundleContext bundleContext;
	protected ServiceTracker<AuthConnectorV1, AuthConnectorV1> serviceTracker;

	public AuthConnectorAdapterV1() {
	}

	public AuthConnectorV1 getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking AuthConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<AuthConnectorV1, AuthConnectorV1>(bundleContext, AuthConnectorV1.class, new ServiceTrackerCustomizer<AuthConnectorV1, AuthConnectorV1>() {
			@Override
			public AuthConnectorV1 addingService(ServiceReference<AuthConnectorV1> reference) {
				AuthConnectorV1 connector = bundleContext.getService(reference);
				LOG.info("addingService() AuthConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<AuthConnectorV1> reference, AuthConnectorV1 connector) {
				LOG.info("removedService() AuthConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<AuthConnectorV1> reference, AuthConnectorV1 connector) {
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

	public void connectorAdded(AuthConnectorV1 connector) {

	}

	public void connectorRemoved(AuthConnectorV1 connector) {

	}

}
