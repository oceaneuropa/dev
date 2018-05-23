package other.orbit.component.api.tier3.domainmanagement;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainServiceConnectorAdapterV1 {

	protected static Logger LOG = LoggerFactory.getLogger(DomainServiceConnectorAdapterV1.class);

	protected ServiceTracker<DomainServiceConnectorV1, DomainServiceConnectorV1> serviceTracker;

	public DomainServiceConnectorAdapterV1() {
	}

	public DomainServiceConnectorV1 getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking DomainServiceConnector.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<DomainServiceConnectorV1, DomainServiceConnectorV1>(bundleContext, DomainServiceConnectorV1.class, new ServiceTrackerCustomizer<DomainServiceConnectorV1, DomainServiceConnectorV1>() {
			@Override
			public DomainServiceConnectorV1 addingService(ServiceReference<DomainServiceConnectorV1> reference) {
				DomainServiceConnectorV1 connector = bundleContext.getService(reference);
				LOG.info("addingService() DomainServiceConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<DomainServiceConnectorV1> reference, DomainServiceConnectorV1 connector) {
				LOG.info("removedService() DomainServiceConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<DomainServiceConnectorV1> reference, DomainServiceConnectorV1 connector) {
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

	public void connectorAdded(DomainServiceConnectorV1 connector) {

	}

	public void connectorRemoved(DomainServiceConnectorV1 connector) {

	}

}
