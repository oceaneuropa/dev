package other.orbit.infra.runtime.channel.ws;

import org.orbit.infra.api.indexes.IndexProviderClient;
import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.orbit.infra.runtime.datatube.ws.DataTubeServiceIndexTimer;
import org.orbit.infra.runtime.datatube.ws.DataTubeWSApplication;
import org.orbit.infra.runtime.datatube.ws.DataTubeWebSocketHandler;
import org.origin.common.rest.server.FeatureConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import other.orbit.infra.api.indexes.IndexProviderLoadBalancer;

public class ChannelServiceAdapterV1 {

	protected static Logger LOG = LoggerFactory.getLogger(ChannelServiceAdapterV1.class);

	protected ServiceTracker<DataTubeService, DataTubeService> serviceTracker;
	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected DataTubeWebSocketHandler webSocket;
	protected DataTubeWSApplication webService;
	protected DataTubeServiceIndexTimer serviceIndexTimer;

	public ChannelServiceAdapterV1(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public DataTubeService getService() {
		return (this.serviceTracker != null) ? this.serviceTracker.getService() : null;
	}

	public void start(final BundleContext bundleContext) {
		LOG.debug("start()");

		this.serviceTracker = new ServiceTracker<DataTubeService, DataTubeService>(bundleContext, DataTubeService.class, new ServiceTrackerCustomizer<DataTubeService, DataTubeService>() {
			@Override
			public DataTubeService addingService(ServiceReference<DataTubeService> reference) {
				DataTubeService service = bundleContext.getService(reference);
				LOG.debug("ServiceTracker ChannelService [" + service + "] is added.");

				doStart(bundleContext, service);

				return service;
			}

			@Override
			public void modifiedService(ServiceReference<DataTubeService> reference, DataTubeService service) {
				LOG.debug("ServiceTracker ChannelService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<DataTubeService> reference, DataTubeService service) {
				LOG.debug("ServiceTracker ChannelService [" + service + "] is removed.");

				doStop(bundleContext, service);
			}
		});
		this.serviceTracker.open();
	}

	public void stop(final BundleContext bundleContext) {
		LOG.debug("stop()");

		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	protected void doStart(BundleContext bundleContext, DataTubeService service) {
		// 1. start web socket
		LOG.debug("start web socket");
		this.webSocket = new DataTubeWebSocketHandler(service);
		this.webSocket.start(bundleContext);

		// 2. start web service
		LOG.debug("start web service");
		this.webService = new DataTubeWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO);
		this.webService.start(bundleContext);

		// 3. start index timer
		LOG.debug("start index timer");
		IndexProviderClient indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		this.serviceIndexTimer = new DataTubeServiceIndexTimer(indexProvider, service);
		this.serviceIndexTimer.start();
	}

	protected void doStop(BundleContext bundleContext, DataTubeService service) {
		// 1. stop index timer
		LOG.debug("stop index timer");
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// 2. stop web service
		LOG.debug("stop web service");
		if (this.webService != null) {
			this.webService.stop(bundleContext);
			this.webService = null;
		}

		// 3. stop web socket
		LOG.debug("stop web socket");
		if (this.webSocket != null) {
			this.webSocket.stop(bundleContext);
			this.webSocket = null;
		}
	}

}
