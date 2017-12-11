package org.orbit.infra.api.indexes;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexServiceConnectorAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(IndexServiceConnectorAdapter.class);

	protected BundleContext bundleContext;
	protected ServiceTracker<IndexServiceConnector, IndexServiceConnector> serviceTracker;

	public IndexServiceConnectorAdapter() {
	}

	public IndexServiceConnector getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking IndexServiceConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<IndexServiceConnector, IndexServiceConnector>(bundleContext, IndexServiceConnector.class, new ServiceTrackerCustomizer<IndexServiceConnector, IndexServiceConnector>() {
			@Override
			public IndexServiceConnector addingService(ServiceReference<IndexServiceConnector> reference) {
				IndexServiceConnector connector = bundleContext.getService(reference);
				LOG.info("addingService() IndexServiceConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<IndexServiceConnector> reference, IndexServiceConnector connector) {
				LOG.info("removedService() IndexServiceConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<IndexServiceConnector> reference, IndexServiceConnector connector) {
				LOG.info("removedService() IndexServiceConnector is removed: " + connector);
				connectorRemoved(connector);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking IndexServiceConnector service.
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	public void connectorAdded(IndexServiceConnector connector) {

	}

	public void connectorRemoved(IndexServiceConnector connector) {

	}

}
