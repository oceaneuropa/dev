package org.orbit.component.api.tier1.config;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigRegistryConnectorAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(ConfigRegistryConnectorAdapter.class);

	protected BundleContext bundleContext;
	protected ServiceTracker<ConfigRegistryConnector, ConfigRegistryConnector> serviceTracker;

	public ConfigRegistryConnectorAdapter() {
	}

	public ConfigRegistryConnector getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking ConfigRegistryConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<ConfigRegistryConnector, ConfigRegistryConnector>(bundleContext, ConfigRegistryConnector.class, new ServiceTrackerCustomizer<ConfigRegistryConnector, ConfigRegistryConnector>() {
			@Override
			public ConfigRegistryConnector addingService(ServiceReference<ConfigRegistryConnector> reference) {
				ConfigRegistryConnector connector = bundleContext.getService(reference);
				LOG.info("addingService() ConfigRegistryConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<ConfigRegistryConnector> reference, ConfigRegistryConnector connector) {
				LOG.info("removedService() ConfigRegistryConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<ConfigRegistryConnector> reference, ConfigRegistryConnector connector) {
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

	public void connectorAdded(ConfigRegistryConnector connector) {

	}

	public void connectorRemoved(ConfigRegistryConnector connector) {

	}

}
