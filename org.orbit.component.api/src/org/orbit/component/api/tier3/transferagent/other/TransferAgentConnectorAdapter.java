package org.orbit.component.api.tier3.transferagent.other;

import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.origin.common.rest.client.ServiceConnector;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferAgentConnectorAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(TransferAgentConnectorAdapter.class);

	protected ServiceTracker<ServiceConnector<TransferAgent>, ServiceConnector<TransferAgent>> serviceTracker;
	protected String connectorId;

	public TransferAgentConnectorAdapter() {
		this.connectorId = "transfer_agent.connector";
	}

	/**
	 * Start tracking TransferAgentConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		Filter filter = null;
		try {
			filter = bundleContext.createFilter("(&(" + Constants.OBJECTCLASS + "=" + ServiceConnector.class.getName() + ")(connector.id=" + this.connectorId + "))");
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}

		this.serviceTracker = new ServiceTracker<ServiceConnector<TransferAgent>, ServiceConnector<TransferAgent>>(bundleContext, filter, new ServiceTrackerCustomizer<ServiceConnector<TransferAgent>, ServiceConnector<TransferAgent>>() {
			@Override
			public ServiceConnector<TransferAgent> addingService(ServiceReference<ServiceConnector<TransferAgent>> reference) {
				ServiceConnector<TransferAgent> connector = bundleContext.getService(reference);
				LOG.info("addingService() ServiceConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<ServiceConnector<TransferAgent>> reference, ServiceConnector<TransferAgent> connector) {
				LOG.info("removedService() ServiceConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<ServiceConnector<TransferAgent>> reference, ServiceConnector<TransferAgent> connector) {
				LOG.info("removedService() ServiceConnector is removed: " + connector);
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

	public void connectorAdded(ServiceConnector<TransferAgent> connector) {

	}

	public void connectorRemoved(ServiceConnector<TransferAgent> connector) {

	}

}

// protected BundleContext bundleContext;
// protected ServiceTracker<TransferAgentConnector, TransferAgentConnector> serviceTracker;

// public TransferAgentConnector getConnector() {
// return this.serviceTracker != null ? this.serviceTracker.getService() : null;
// }

// this.serviceTracker = new ServiceTracker<TransferAgentConnector, TransferAgentConnector>(bundleContext, TransferAgentConnector.class, new
// ServiceTrackerCustomizer<TransferAgentConnector, TransferAgentConnector>() {
// @Override
// public TransferAgentConnector addingService(ServiceReference<TransferAgentConnector> reference) {
// TransferAgentConnector connector = bundleContext.getService(reference);
// LOG.info("addingService() TransferAgentConnector is added: " + connector);
// connectorAdded(connector);
// return connector;
// }
//
// @Override
// public void modifiedService(ServiceReference<TransferAgentConnector> reference, TransferAgentConnector connector) {
// LOG.info("removedService() TransferAgentConnector is modified: " + connector);
// }
//
// @Override
// public void removedService(ServiceReference<TransferAgentConnector> reference, TransferAgentConnector connector) {
// LOG.info("removedService() TransferAgentConnector is removed: " + connector);
// connectorRemoved(connector);
// }
// });
// this.serviceTracker.open();

// if (this.serviceTracker != null) {
// this.serviceTracker.close();
// this.serviceTracker = null;
// }

// public void connectorAdded(TransferAgentConnector connector) {
//
// }
//
// public void connectorRemoved(TransferAgentConnector connector) {
//
// }