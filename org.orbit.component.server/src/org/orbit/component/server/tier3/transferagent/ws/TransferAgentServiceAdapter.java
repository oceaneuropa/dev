package org.orbit.component.server.tier3.transferagent.ws;

import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Adapter to start TransferAgentWSApplication when TransferAgentService becomes available and to stop TransferAgentWSApplication when TransferAgentService
 * becomes unavailable.
 * 
 */
public class TransferAgentServiceAdapter {

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<TransferAgentService, TransferAgentService> serviceTracker;
	protected TransferAgentWSApplication webApp;

	public TransferAgentServiceAdapter() {
	}

	public TransferAgentServiceAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public TransferAgentService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
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
		this.serviceTracker = new ServiceTracker<TransferAgentService, TransferAgentService>(bundleContext, TransferAgentService.class, new ServiceTrackerCustomizer<TransferAgentService, TransferAgentService>() {
			@Override
			public TransferAgentService addingService(ServiceReference<TransferAgentService> reference) {
				TransferAgentService service = bundleContext.getService(reference);
				System.out.println("TransferAgentService [" + service + "] is added.");

				startWebService(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<TransferAgentService> reference, TransferAgentService service) {
				System.out.println("TransferAgentService [" + service + "] is modified.");

				stopWebService(bundleContext, service);
				startWebService(bundleContext, service);
			}

			@Override
			public void removedService(ServiceReference<TransferAgentService> reference, TransferAgentService service) {
				System.out.println("TransferAgentService [" + service + "] is removed.");

				stopWebService(bundleContext, service);
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
	 * @param service
	 */
	protected void startWebService(BundleContext bundleContext, TransferAgentService service) {
		// Start web service
		this.webApp = new TransferAgentWSApplication();
		this.webApp.setBundleContext(bundleContext);
		this.webApp.setContextRoot(service.getContextRoot());
		if (this.indexProviderLoadBalancer != null) {
			this.webApp.setIndexProvider(this.indexProviderLoadBalancer.createLoadBalancableIndexProvider());
		}
		this.webApp.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void stopWebService(BundleContext bundleContext, TransferAgentService service) {
		if (this.webApp != null) {
			// Stop web service
			this.webApp.stop();
			this.webApp = null;
		}
	}

}
