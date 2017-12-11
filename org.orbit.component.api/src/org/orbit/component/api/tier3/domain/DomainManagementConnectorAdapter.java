package org.orbit.component.api.tier3.domain;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainManagementConnectorAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(DomainManagementConnectorAdapter.class);

	protected BundleContext bundleContext;
	protected ServiceTracker<DomainManagementConnector, DomainManagementConnector> serviceTracker;

	public DomainManagementConnectorAdapter() {
	}

	public DomainManagementConnector getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking DomainManagementConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<DomainManagementConnector, DomainManagementConnector>(bundleContext, DomainManagementConnector.class, new ServiceTrackerCustomizer<DomainManagementConnector, DomainManagementConnector>() {
			@Override
			public DomainManagementConnector addingService(ServiceReference<DomainManagementConnector> reference) {
				DomainManagementConnector connector = bundleContext.getService(reference);
				LOG.info("addingService() DomainManagementConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<DomainManagementConnector> reference, DomainManagementConnector connector) {
				LOG.info("removedService() DomainManagementConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<DomainManagementConnector> reference, DomainManagementConnector connector) {
				LOG.info("removedService() DomainManagementConnector is removed: " + connector);
				connectorRemoved(connector);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking DomainManagementConnector service.
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	public void connectorAdded(DomainManagementConnector connector) {

	}

	public void connectorRemoved(DomainManagementConnector connector) {

	}

}
