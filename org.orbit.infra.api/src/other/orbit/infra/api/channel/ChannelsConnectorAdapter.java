package other.orbit.infra.api.channel;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelsConnectorAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(ChannelsConnectorAdapter.class);

	protected BundleContext bundleContext;
	protected ServiceTracker<ChannelsConnector, ChannelsConnector> serviceTracker;

	public ChannelsConnectorAdapter() {
	}

	public ChannelsConnector getConnector() {
		return this.serviceTracker != null ? this.serviceTracker.getService() : null;
	}

	/**
	 * Start tracking ChannelsConnector service.
	 * 
	 */
	public void start(BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<ChannelsConnector, ChannelsConnector>(bundleContext, ChannelsConnector.class, new ServiceTrackerCustomizer<ChannelsConnector, ChannelsConnector>() {
			@Override
			public ChannelsConnector addingService(ServiceReference<ChannelsConnector> reference) {
				ChannelsConnector connector = bundleContext.getService(reference);
				LOG.info("addingService() ChannelsConnector is added: " + connector);
				connectorAdded(connector);
				return connector;
			}

			@Override
			public void modifiedService(ServiceReference<ChannelsConnector> reference, ChannelsConnector connector) {
				LOG.info("removedService() ChannelsConnector is modified: " + connector);
			}

			@Override
			public void removedService(ServiceReference<ChannelsConnector> reference, ChannelsConnector connector) {
				LOG.info("removedService() ChannelsConnector is removed: " + connector);
				connectorRemoved(connector);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * Stop tracking ChannelsConnector service.
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	public void connectorAdded(ChannelsConnector connector) {

	}

	public void connectorRemoved(ChannelsConnector connector) {

	}

}
