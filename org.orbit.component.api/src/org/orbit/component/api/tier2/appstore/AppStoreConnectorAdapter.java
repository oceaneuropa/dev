package org.orbit.component.api.tier2.appstore;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppStoreConnectorAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(AppStoreConnectorAdapter.class);

	protected ServiceTracker<AppStoreConnector, AppStoreConnector> serviceTracker;

	public AppStoreConnectorAdapter() {
	}

	public AppStoreConnector getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking AppStoreConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<AppStoreConnector, AppStoreConnector>(bundleContext, AppStoreConnector.class, new ServiceTrackerCustomizer<AppStoreConnector, AppStoreConnector>() {
			@Override
			public AppStoreConnector addingService(ServiceReference<AppStoreConnector> reference) {
				AppStoreConnector connector = bundleContext.getService(reference);
				LOG.info("addingService() AppStoreConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<AppStoreConnector> reference, AppStoreConnector connector) {
				LOG.info("removedService() AppStoreConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<AppStoreConnector> reference, AppStoreConnector connector) {
				LOG.info("removedService() AppStoreConnector is removed: " + connector);
				connectorRemoved(connector);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking AppStoreConnector service.
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	public void connectorAdded(AppStoreConnector connector) {

	}

	public void connectorRemoved(AppStoreConnector connector) {

	}

}
