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

public class ServiceConnectorAdapterV2 {

	protected static Logger LOG = LoggerFactory.getLogger(ServiceConnectorAdapterV2.class);

	protected String serviceId;
	protected BundleContext bundleContext;
	protected ServiceTracker<ServiceConnector<Object>, ServiceConnector<Object>> serviceTracker;

	/**
	 * 
	 * @param serviceId
	 *            e.g. "transfer_agent.connector" or TransferAgent.class.getName()
	 */
	public ServiceConnectorAdapterV2(String serviceId) {
		this.serviceId = serviceId;
	}

	public ServiceConnector<?> getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	@SuppressWarnings("unchecked")
	public <SERVICE> SERVICE getService(Class<SERVICE> clazz, Map<String, Object> properties) throws ClientException {
		SERVICE service = null;
		if (this.serviceTracker != null) {
			ServiceConnector<Object> connector = this.serviceTracker.getService();
			if (connector != null) {
				String currServiceId = connector.getServiceId();
				if (this.serviceId.equals(currServiceId)) {
					Object object = connector.getService(properties);
					if (object != null && clazz.isAssignableFrom(object.getClass())) {
						service = (SERVICE) object;
					}
				}
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
		this.serviceTracker = new ServiceTracker<ServiceConnector<Object>, ServiceConnector<Object>>(bundleContext, filter, new ServiceTrackerCustomizer<ServiceConnector<Object>, ServiceConnector<Object>>() {
			@Override
			public ServiceConnector<Object> addingService(ServiceReference<ServiceConnector<Object>> reference) {
				ServiceConnector<Object> connector = bundleContext.getService(reference);
				LOG.info("addingService() ServiceConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<ServiceConnector<Object>> reference, ServiceConnector<Object> connector) {
				LOG.info("removedService() ServiceConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<ServiceConnector<Object>> reference, ServiceConnector<Object> connector) {
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

	public void connectorAdded(ServiceConnector<?> connector) {

	}

	public void connectorRemoved(ServiceConnector<?> connector) {

	}

}
