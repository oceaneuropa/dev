package org.origin.common.rest.client;

import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceConnectorAdapter<SERVICE> {

	protected static Logger LOG = LoggerFactory.getLogger(ServiceConnectorAdapter.class);

	protected Class<SERVICE> serviceClass;
	protected ServiceTracker<ServiceConnector<Object>, ServiceConnector<Object>> serviceTracker;

	/**
	 * @param serviceClass
	 */
	public ServiceConnectorAdapter(Class<SERVICE> serviceClass) {
		if (serviceClass == null) {
			throw new IllegalArgumentException("serviceClass is null.");
		}
		this.serviceClass = serviceClass;
	}

	protected String getServiceId() {
		return this.serviceClass.getName();
	}

	public ServiceConnector<?> getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	@SuppressWarnings("unchecked")
	public SERVICE getService(Map<String, Object> properties) {
		SERVICE service = null;
		if (this.serviceTracker != null) {
			ServiceConnector<Object> connector = this.serviceTracker.getService();
			if (connector != null) {
				String serviceId = getServiceId();
				String currServiceId = connector.getServiceId();
				if (currServiceId != null && currServiceId.equals(serviceId)) {
					Object object = connector.getService(properties);
					if (object != null && this.serviceClass.isAssignableFrom(object.getClass())) {
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
			filter = bundleContext.createFilter("(&(" + Constants.OBJECTCLASS + "=" + ServiceConnector.class.getName() + ")(" + ServiceConnector.CONNECTOR_SERVICE_ID + "=" + getServiceId() + "))");
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
		this.serviceTracker = new ServiceTracker<ServiceConnector<Object>, ServiceConnector<Object>>(bundleContext, filter, new ServiceTrackerCustomizer<ServiceConnector<Object>, ServiceConnector<Object>>() {
			@Override
			public ServiceConnector<Object> addingService(ServiceReference<ServiceConnector<Object>> reference) {
				ServiceConnector<Object> connector = bundleContext.getService(reference);
				LOG.info("addingService() ServiceConnector is added: " + connector);

				notifyConnectorAdded((ServiceConnector<SERVICE>) connector, 1000);

				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<ServiceConnector<Object>> reference, ServiceConnector<Object> connector) {
				LOG.info("removedService() ServiceConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<ServiceConnector<Object>> reference, ServiceConnector<Object> connector) {
				LOG.info("removedService() ServiceConnector is removed: " + connector);

				notifyConnectorRemoved((ServiceConnector<SERVICE>) connector, 1000);
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

	public void notifyConnectorAdded(final ServiceConnector<SERVICE> connector, final long delay) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if (delay > 0) {
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				connectorAdded((ServiceConnector<SERVICE>) connector);
			}
		});
		t.start();
	}

	public void notifyConnectorRemoved(final ServiceConnector<SERVICE> connector, final long delay) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if (delay > 0) {
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				connectorRemoved((ServiceConnector<SERVICE>) connector);
			}
		});
		t.start();
	}

	public void connectorAdded(ServiceConnector<SERVICE> connector) {

	}

	public void connectorRemoved(ServiceConnector<SERVICE> connector) {

	}

}
