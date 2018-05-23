package other.orbit.infra.api.indexes;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexProviderConnectorAdapterV1 {

	protected static Logger LOG = LoggerFactory.getLogger(IndexProviderConnectorAdapterV1.class);

	protected BundleContext bundleContext;
	protected ServiceTracker<IndexProviderConnectorV1, IndexProviderConnectorV1> serviceTracker;

	public IndexProviderConnectorAdapterV1() {
	}

	public IndexProviderConnectorV1 getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking IndexProviderConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<IndexProviderConnectorV1, IndexProviderConnectorV1>(bundleContext, IndexProviderConnectorV1.class, new ServiceTrackerCustomizer<IndexProviderConnectorV1, IndexProviderConnectorV1>() {
			@Override
			public IndexProviderConnectorV1 addingService(ServiceReference<IndexProviderConnectorV1> reference) {
				IndexProviderConnectorV1 connector = bundleContext.getService(reference);
				LOG.info("addingService() IndexProviderConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<IndexProviderConnectorV1> reference, IndexProviderConnectorV1 connector) {
				LOG.info("removedService() IndexProviderConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<IndexProviderConnectorV1> reference, IndexProviderConnectorV1 connector) {
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

	public void connectorAdded(IndexProviderConnectorV1 connector) {

	}

	public void connectorRemoved(IndexProviderConnectorV1 connector) {

	}

}
