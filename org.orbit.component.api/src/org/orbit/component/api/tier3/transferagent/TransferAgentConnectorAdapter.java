package org.orbit.component.api.tier3.transferagent;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferAgentConnectorAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(TransferAgentConnectorAdapter.class);

	protected BundleContext bundleContext;
	protected ServiceTracker<TransferAgentConnector, TransferAgentConnector> serviceTracker;

	public TransferAgentConnectorAdapter() {
	}

	public TransferAgentConnector getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking TransferAgentConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<TransferAgentConnector, TransferAgentConnector>(bundleContext, TransferAgentConnector.class, new ServiceTrackerCustomizer<TransferAgentConnector, TransferAgentConnector>() {
			@Override
			public TransferAgentConnector addingService(ServiceReference<TransferAgentConnector> reference) {
				TransferAgentConnector connector = bundleContext.getService(reference);
				LOG.info("addingService() TransferAgentConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<TransferAgentConnector> reference, TransferAgentConnector connector) {
				LOG.info("removedService() TransferAgentConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<TransferAgentConnector> reference, TransferAgentConnector connector) {
				LOG.info("removedService() TransferAgentConnector is removed: " + connector);
				connectorRemoved(connector);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking TransferAgentConnector service.
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	public void connectorAdded(TransferAgentConnector connector) {

	}

	public void connectorRemoved(TransferAgentConnector connector) {

	}

}
