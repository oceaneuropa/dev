package org.orbit.infra.api.indexes;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexProviderConnectorAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(IndexProviderConnectorAdapter.class);

	protected BundleContext bundleContext;
	protected ServiceTracker<IndexProviderConnector, IndexProviderConnector> serviceTracker;

	public IndexProviderConnectorAdapter() {
	}

	public IndexProviderConnector getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking IndexProviderConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<IndexProviderConnector, IndexProviderConnector>(bundleContext, IndexProviderConnector.class, new ServiceTrackerCustomizer<IndexProviderConnector, IndexProviderConnector>() {
			@Override
			public IndexProviderConnector addingService(ServiceReference<IndexProviderConnector> reference) {
				IndexProviderConnector connector = bundleContext.getService(reference);
				LOG.info("addingService() IndexProviderConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<IndexProviderConnector> reference, IndexProviderConnector connector) {
				LOG.info("removedService() IndexProviderConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<IndexProviderConnector> reference, IndexProviderConnector connector) {
				LOG.info("removedService() IndexProviderConnector is removed: " + connector);
				connectorRemoved(connector);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking IndexProviderConnector service.
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	public void connectorAdded(IndexProviderConnector connector) {

	}

	public void connectorRemoved(IndexProviderConnector connector) {

	}

}
