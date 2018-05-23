package other.orbit.component.api.tier2.appstore;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppStoreConnectorAdapterV1 {

	protected static Logger LOG = LoggerFactory.getLogger(AppStoreConnectorAdapterV1.class);

	protected ServiceTracker<AppStoreConnectorV1, AppStoreConnectorV1> serviceTracker;

	public AppStoreConnectorAdapterV1() {
	}

	public AppStoreConnectorV1 getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking AppStoreConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<AppStoreConnectorV1, AppStoreConnectorV1>(bundleContext, AppStoreConnectorV1.class, new ServiceTrackerCustomizer<AppStoreConnectorV1, AppStoreConnectorV1>() {
			@Override
			public AppStoreConnectorV1 addingService(ServiceReference<AppStoreConnectorV1> reference) {
				AppStoreConnectorV1 connector = bundleContext.getService(reference);
				LOG.info("addingService() AppStoreConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<AppStoreConnectorV1> reference, AppStoreConnectorV1 connector) {
				LOG.info("removedService() AppStoreConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<AppStoreConnectorV1> reference, AppStoreConnectorV1 connector) {
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

	public void connectorAdded(AppStoreConnectorV1 connector) {

	}

	public void connectorRemoved(AppStoreConnectorV1 connector) {

	}

}
