package other.orbit.component.api.tier1.auth;

import org.orbit.component.api.tier1.auth.Auth;
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

public class AuthConnectorAdapterV0 {

	protected static Logger LOG = LoggerFactory.getLogger(AuthConnectorAdapterV0.class);

	protected ServiceTracker<ServiceConnector<Auth>, ServiceConnector<Auth>> serviceTracker;
	protected String connectorId;

	public AuthConnectorAdapterV0() {
		this.connectorId = "auth.connector";
	}

	/**
	 * Start tracking AuthConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		Filter filter = null;
		try {
			filter = bundleContext.createFilter("(&(" + Constants.OBJECTCLASS + "=" + ServiceConnector.class.getName() + ")(connector.id=" + this.connectorId + "))");
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}

		this.serviceTracker = new ServiceTracker<ServiceConnector<Auth>, ServiceConnector<Auth>>(bundleContext, filter, new ServiceTrackerCustomizer<ServiceConnector<Auth>, ServiceConnector<Auth>>() {
			@Override
			public ServiceConnector<Auth> addingService(ServiceReference<ServiceConnector<Auth>> reference) {
				ServiceConnector<Auth> connector = bundleContext.getService(reference);
				LOG.info("addingService() ServiceConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<ServiceConnector<Auth>> reference, ServiceConnector<Auth> connector) {
				LOG.info("removedService() ServiceConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<ServiceConnector<Auth>> reference, ServiceConnector<Auth> connector) {
				LOG.info("removedService() ServiceConnector is removed: " + connector);
				connectorRemoved(connector);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking AuthConnector service.
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	public void connectorAdded(ServiceConnector<Auth> connector) {

	}

	public void connectorRemoved(ServiceConnector<Auth> connector) {

	}

}
