package org.orbit.infra.runtime.channel.ws;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.runtime.channel.service.ChannelService;
import org.origin.common.rest.server.FeatureConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelServiceAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(ChannelServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<ChannelService, ChannelService> serviceTracker;
	protected ChannelWebSocket webSocket;
	protected ChannelWSApplication webService;
	protected ChannelServiceIndexTimer serviceIndexTimer;

	public ChannelServiceAdapter(Map<Object, Object> properties) {
		this.properties = checkProperties(properties);
	}

	protected Map<Object, Object> checkProperties(Map<Object, Object> properties) {
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		return properties;
	}

	public ChannelService getService() {
		return (this.serviceTracker != null) ? this.serviceTracker.getService() : null;
	}

	public IndexProvider getIndexProvider() {
		return InfraClients.getInstance().getIndexProviderProxy(this.properties);
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
		this.webService = new ChannelWSApplication(service, FeatureConstants.PING | FeatureConstants.ECHO);
		this.webService.start(bundleContext);

		// 3. start index timer
		LOG.debug("start index timer");
		IndexProvider indexProvider = getIndexProvider();
		this.serviceIndexTimer = new ChannelServiceIndexTimer(indexProvider, service);
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
