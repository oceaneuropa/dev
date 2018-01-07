package org.orbit.infra.api.indexes.other;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexServiceConnectorAdapterV1 {

	protected static Logger LOG = LoggerFactory.getLogger(IndexServiceConnectorAdapterV1.class);

	protected BundleContext bundleContext;
	protected ServiceTracker<IndexServiceConnectorV1, IndexServiceConnectorV1> serviceTracker;

	public IndexServiceConnectorAdapterV1() {
	}

	public IndexServiceConnectorV1 getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking IndexServiceConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<IndexServiceConnectorV1, IndexServiceConnectorV1>(bundleContext, IndexServiceConnectorV1.class, new ServiceTrackerCustomizer<IndexServiceConnectorV1, IndexServiceConnectorV1>() {
			@Override
			public IndexServiceConnectorV1 addingService(ServiceReference<IndexServiceConnectorV1> reference) {
				IndexServiceConnectorV1 connector = bundleContext.getService(reference);
				LOG.info("addingService() IndexServiceConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<IndexServiceConnectorV1> reference, IndexServiceConnectorV1 connector) {
				LOG.info("removedService() IndexServiceConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<IndexServiceConnectorV1> reference, IndexServiceConnectorV1 connector) {
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

	public void connectorAdded(IndexServiceConnectorV1 connector) {

	}

	public void connectorRemoved(IndexServiceConnectorV1 connector) {

	}

}
