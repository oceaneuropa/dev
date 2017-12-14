package org.orbit.component.api.tier3.transferagent;

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

	protected BundleContext bundleContext;
	protected ServiceTracker<TransferAgentConnector, TransferAgentConnector> serviceTracker;
	protected ServiceTracker<ServiceConnector<TransferAgent>, ServiceConnector<TransferAgent>> serviceTracker2;
	protected String connectorId;

	public TransferAgentConnectorAdapter() {
		this.connectorId = "transfer_agent.connector";
	}

	public TransferAgentConnector getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking TransferAgentConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
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

		Filter filter = null;
		try {
			filter = bundleContext.createFilter("(&(" + Constants.OBJECTCLASS + "=" + ServiceConnector.class.getName() + ")(connector.id=" + this.connectorId + "))");
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
		this.serviceTracker2 = new ServiceTracker<ServiceConnector<TransferAgent>, ServiceConnector<TransferAgent>>(bundleContext, filter, new ServiceTrackerCustomizer<ServiceConnector<TransferAgent>, ServiceConnector<TransferAgent>>() {
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
		this.serviceTracker2.open();
	}

	/**
	 * Stop tracking TransferAgentConnector service.
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		// if (this.serviceTracker != null) {
		// this.serviceTracker.close();
		// this.serviceTracker = null;
		// }

		if (this.serviceTracker2 != null) {
			this.serviceTracker2.close();
			this.serviceTracker2 = null;
		}
	}

	public void connectorAdded(TransferAgentConnector connector) {

	}

	public void connectorRemoved(TransferAgentConnector connector) {

	}

	public void connectorAdded(ServiceConnector<TransferAgent> connector) {

	}

	public void connectorRemoved(ServiceConnector<TransferAgent> connector) {

	}

}
