package org.orbit.os.server.ws;

import org.orbit.os.server.service.NodeOS;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class NodeOSAdapter {

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<NodeOS, NodeOS> serviceTracker;
	protected NodeOSWSApplication webApp;

	public NodeOSAdapter() {
	}

	public NodeOSAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public NodeOS getService() {
		return (this.serviceTracker != null) ? this.serviceTracker.getService() : null;
	}

	public IndexProviderLoadBalancer getIndexProviderLoadBalancer() {
		return indexProviderLoadBalancer;
	}

	public void setIndexProviderLoadBalancer(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
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
				System.out.println(nodeOS.getLabel() + " is added.");

				startWebService(bundleContext, nodeOS);
				return nodeOS;
			}

			@Override
			public void modifiedService(ServiceReference<NodeOS> reference, NodeOS nodeOS) {
				System.out.println(nodeOS.getLabel() + " is modified.");

				stopWebService(bundleContext, nodeOS);
				startWebService(bundleContext, nodeOS);
			}

			@Override
			public void removedService(ServiceReference<NodeOS> reference, NodeOS nodeOS) {
				System.out.println(nodeOS.getLabel() + " is removed.");

				stopWebService(bundleContext, nodeOS);
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
	protected void startWebService(BundleContext bundleContext, NodeOS nodeOS) {
		this.webApp = new NodeOSWSApplication();
		this.webApp.setBundleContext(bundleContext);
		this.webApp.setContextRoot(nodeOS.getContextRoot());
		this.webApp.setIndexProvider(this.indexProviderLoadBalancer.createLoadBalancableIndexProvider());
		this.webApp.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param nodeOS
	 */
	protected void stopWebService(BundleContext bundleContext, NodeOS nodeOS) {
		if (this.webApp != null) {
			this.webApp.stop();
			this.webApp = null;
		}
	}

}
