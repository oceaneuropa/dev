package org.orbit.os.server.ws;

import org.orbit.os.server.service.NodeOS;
import org.origin.mgm.client.api.IndexProvider;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeOSAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(NodeOSAdapter.class);

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<NodeOS, NodeOS> serviceTracker;
	protected NodeOSWebServiceApplication webApp;
	protected IndexProvider indexProvider;
	protected NodeOSIndexTimer serviceIndexTimer;

	public NodeOSAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public NodeOS getService() {
		return (this.serviceTracker != null) ? this.serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<NodeOS, NodeOS>(bundleContext, NodeOS.class, new ServiceTrackerCustomizer<NodeOS, NodeOS>() {
			@Override
			public NodeOS addingService(ServiceReference<NodeOS> reference) {
				NodeOS nodeOS = bundleContext.getService(reference);
				doStart(bundleContext, nodeOS);
				return nodeOS;
			}

			@Override
			public void modifiedService(ServiceReference<NodeOS> reference, NodeOS nodeOS) {
			}

			@Override
			public void removedService(ServiceReference<NodeOS> reference, NodeOS nodeOS) {
				doStop(bundleContext, nodeOS);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @param nodeOS
	 */
	protected void doStart(BundleContext bundleContext, NodeOS nodeOS) {
		LOG.info("doStart()");

		if (this.indexProvider == null) {
			this.indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		}

		// Start web service
		this.webApp = new NodeOSWebServiceApplication(bundleContext, nodeOS);
		this.webApp.start();

		// Start index timer
		this.serviceIndexTimer = new NodeOSIndexTimer(this.indexProvider);
		this.serviceIndexTimer.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param nodeOS
	 */
	protected void doStop(BundleContext bundleContext, NodeOS nodeOS) {
		LOG.info("doStop()");

		// Start index timer
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// Stop webService
		if (this.webApp != null) {
			this.webApp.stop();
			this.webApp = null;
		}
	}

}
