package other.orbit.component.api.tier1.account;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistryConnectorAdapterV1 {

	protected static Logger LOG = LoggerFactory.getLogger(UserRegistryConnectorAdapterV1.class);

	protected ServiceTracker<UserRegistryConnectorV1, UserRegistryConnectorV1> serviceTracker;

	public UserRegistryConnectorAdapterV1() {
	}

	public UserRegistryConnectorV1 getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking UserRegistryConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<UserRegistryConnectorV1, UserRegistryConnectorV1>(bundleContext, UserRegistryConnectorV1.class, new ServiceTrackerCustomizer<UserRegistryConnectorV1, UserRegistryConnectorV1>() {
			@Override
			public UserRegistryConnectorV1 addingService(ServiceReference<UserRegistryConnectorV1> reference) {
				UserRegistryConnectorV1 connector = bundleContext.getService(reference);
				LOG.info("addingService() UserRegistryConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<UserRegistryConnectorV1> reference, UserRegistryConnectorV1 connector) {
				LOG.info("removedService() UserRegistryConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<UserRegistryConnectorV1> reference, UserRegistryConnectorV1 connector) {
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

	public void connectorAdded(UserRegistryConnectorV1 connector) {

	}

	public void connectorRemoved(UserRegistryConnectorV1 connector) {

	}

}
