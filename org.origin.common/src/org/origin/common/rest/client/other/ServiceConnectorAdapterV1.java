package org.origin.common.rest.client.other;

import java.util.Map;

import org.origin.common.rest.client.ClientException;
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

public class ServiceConnectorAdapterV1<SERVICE> {

	protected static Logger LOG = LoggerFactory.getLogger(ServiceConnectorAdapterV1.class);

	protected String serviceId;
	protected BundleContext bundleContext;
	protected ServiceTracker<ServiceConnector<SERVICE>, ServiceConnector<SERVICE>> serviceTracker;

	/**
	 * 
	 * @param serviceId
	 *            e.g. "transfer_agent.connector" or TransferAgent.class.getName()
	 */
	public ServiceConnectorAdapterV1(String serviceId) {
		this.serviceId = serviceId;
	}

	public ServiceConnector<SERVICE> getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	public SERVICE getService(Map<Object, Object> properties) throws ClientException {
		SERVICE service = null;
		if (this.serviceTracker != null) {
			ServiceConnector<SERVICE> connector = this.serviceTracker.getService();
			if (connector != null) {
				service = connector.getService(properties);
			}
		}
		return service;
	}

	/**
	 * Start tracking Connector service.
	 * 
	 */
	public void start(final BundleContext bundleContext) {
		Filter filter = null;
		try {
			filter = bundleContext.createFilter("(&(" + Constants.OBJECTCLASS + "=" + ServiceConnector.class.getName() + ")(" + ServiceConnector.CONNECTOR_SERVICE_ID + "=" + this.serviceId + "))");
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
		this.serviceTracker = new ServiceTracker<ServiceConnector<SERVICE>, ServiceConnector<SERVICE>>(bundleContext, filter, new ServiceTrackerCustomizer<ServiceConnector<SERVICE>, ServiceConnector<SERVICE>>() {
			@Override
			public ServiceConnector<SERVICE> addingService(ServiceReference<ServiceConnector<SERVICE>> reference) {
				ServiceConnector<SERVICE> connector = bundleContext.getService(reference);
				LOG.info("addingService() ServiceConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<ServiceConnector<SERVICE>> reference, ServiceConnector<SERVICE> connector) {
				LOG.info("removedService() ServiceConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<ServiceConnector<SERVICE>> reference, ServiceConnector<SERVICE> connector) {
				LOG.info("removedService() ServiceConnector is removed: " + connector);
				connectorRemoved(connector);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking Connector service.
	 * 
	 */
	public void stop(final BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	public void connectorAdded(ServiceConnector<SERVICE> connector) {

	}

	public void connectorRemoved(ServiceConnector<SERVICE> connector) {

	}

}
