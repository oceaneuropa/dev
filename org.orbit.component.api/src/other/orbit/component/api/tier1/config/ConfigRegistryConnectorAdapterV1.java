package other.orbit.component.api.tier1.config;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigRegistryConnectorAdapterV1 {

	protected static Logger LOG = LoggerFactory.getLogger(ConfigRegistryConnectorAdapterV1.class);

	protected ServiceTracker<ConfigRegistryConnectorV1, ConfigRegistryConnectorV1> serviceTracker;

	public ConfigRegistryConnectorAdapterV1() {
	}

	public ConfigRegistryConnectorV1 getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking ConfigRegistryConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<ConfigRegistryConnectorV1, ConfigRegistryConnectorV1>(bundleContext, ConfigRegistryConnectorV1.class, new ServiceTrackerCustomizer<ConfigRegistryConnectorV1, ConfigRegistryConnectorV1>() {
			@Override
			public ConfigRegistryConnectorV1 addingService(ServiceReference<ConfigRegistryConnectorV1> reference) {
				ConfigRegistryConnectorV1 connector = bundleContext.getService(reference);
				LOG.info("addingService() ConfigRegistryConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<ConfigRegistryConnectorV1> reference, ConfigRegistryConnectorV1 connector) {
				LOG.info("removedService() ConfigRegistryConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<ConfigRegistryConnectorV1> reference, ConfigRegistryConnectorV1 connector) {
				LOG.info("removedService() ConfigRegistryConnector is removed: " + connector);
				connectorRemoved(connector);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking ConfigRegistryConnector service.
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	public void connectorAdded(ConfigRegistryConnectorV1 connector) {

	}

	public void connectorRemoved(ConfigRegistryConnectorV1 connector) {

	}

}
