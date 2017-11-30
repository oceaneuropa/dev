package org.orbit.component.server.tier0.channel.ws;

import org.orbit.component.server.tier0.channel.service.ChannelService;
import org.origin.mgm.client.api.IndexProvider;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelServiceAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(ChannelServiceAdapter.class);

	protected ServiceTracker<ChannelService, ChannelService> serviceTracker;
	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected IndexProvider indexProvider;
	protected ChannelWebSocket webSocket;
	protected ChannelWebServiceApplication webService;
	protected ChannelServiceIndexTimer serviceIndexTimer;

	public ChannelServiceAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public ChannelService getService() {
		return (this.serviceTracker != null) ? this.serviceTracker.getService() : null;
	}

	public void start(final BundleContext bundleContext) {
		LOG.debug("start()");

		this.serviceTracker = new ServiceTracker<ChannelService, ChannelService>(bundleContext, ChannelService.class, new ServiceTrackerCustomizer<ChannelService, ChannelService>() {
			@Override
			public ChannelService addingService(ServiceReference<ChannelService> reference) {
				ChannelService service = bundleContext.getService(reference);
				LOG.debug("ServiceTracker ChannelService [" + service + "] is added.");

				doStart(bundleContext, service);

				return service;
			}

			@Override
			public void modifiedService(ServiceReference<ChannelService> reference, ChannelService service) {
				LOG.debug("ServiceTracker ChannelService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<ChannelService> reference, ChannelService service) {
				LOG.debug("ServiceTracker ChannelService [" + service + "] is removed.");

				doStop(bundleContext, service);
			}
		});
		this.serviceTracker.open();

		this.indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
	}

	public void stop(final BundleContext bundleContext) {
		LOG.debug("stop()");

		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	protected void doStart(BundleContext bundleContext, ChannelService service) {
		// 1. start web socket
		LOG.debug("start web socket");
		this.webSocket = new ChannelWebSocket(service);
		this.webSocket.start(bundleContext);

		// 2. start web service
		LOG.debug("start web service");
		this.webService = new ChannelWebServiceApplication(bundleContext, service);
		this.webService.start();

		// 3. start index timer
		LOG.debug("start index timer");
		this.serviceIndexTimer = new ChannelServiceIndexTimer(this.indexProvider, service);
		this.serviceIndexTimer.start();
	}

	protected void doStop(BundleContext bundleContext, ChannelService service) {
		// 1. stop index timer
		LOG.debug("stop index timer");
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// 2. stop web service
		LOG.debug("stop web service");
		if (this.webService != null) {
			this.webService.stop();
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
