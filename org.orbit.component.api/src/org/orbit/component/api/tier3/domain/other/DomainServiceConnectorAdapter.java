package org.orbit.component.api.tier3.domain.other;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainServiceConnectorAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(DomainServiceConnectorAdapter.class);

	protected ServiceTracker<DomainServiceConnector, DomainServiceConnector> serviceTracker;

	public DomainServiceConnectorAdapter() {
	}

	public DomainServiceConnector getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking DomainServiceConnector.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<DomainServiceConnector, DomainServiceConnector>(bundleContext, DomainServiceConnector.class, new ServiceTrackerCustomizer<DomainServiceConnector, DomainServiceConnector>() {
			@Override
			public DomainServiceConnector addingService(ServiceReference<DomainServiceConnector> reference) {
				DomainServiceConnector connector = bundleContext.getService(reference);
				LOG.info("addingService() DomainServiceConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<DomainServiceConnector> reference, DomainServiceConnector connector) {
				LOG.info("removedService() DomainServiceConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<DomainServiceConnector> reference, DomainServiceConnector connector) {
				LOG.info("removedService() DomainServiceConnector is removed: " + connector);
				connectorRemoved(connector);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking DomainServiceConnector.
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	public void connectorAdded(DomainServiceConnector connector) {

	}

	public void connectorRemoved(DomainServiceConnector connector) {

	}

}
