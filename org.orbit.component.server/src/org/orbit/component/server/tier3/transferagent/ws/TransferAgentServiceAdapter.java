package org.orbit.component.server.tier3.transferagent.ws;

import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.mgm.client.api.IndexProvider;
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
	protected TransferAgentWebServiceApplication webServiceApp;
	protected TransferAgentServiceTimer serviceIndexTimer;

	public TransferAgentServiceAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public TransferAgentService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
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

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<TransferAgentService> reference, TransferAgentService service) {
				System.out.println("TransferAgentService [" + service + "] is modified.");

				doStop(bundleContext, service);
				doStart(bundleContext, service);
			}

			@Override
			public void removedService(ServiceReference<TransferAgentService> reference, TransferAgentService service) {
				System.out.println("TransferAgentService [" + service + "] is removed.");

				doStop(bundleContext, service);
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
	protected void doStart(BundleContext bundleContext, TransferAgentService service) {
		// Start web service
		this.webServiceApp = new TransferAgentWebServiceApplication(bundleContext, service);
		this.webServiceApp.setBundleContext(bundleContext);
		this.webServiceApp.setContextRoot(service.getContextRoot());
		this.webServiceApp.start();

		// Start index timer
		IndexProvider indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		this.serviceIndexTimer = new TransferAgentServiceTimer(indexProvider, service);
		this.serviceIndexTimer.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, TransferAgentService service) {
		// Stop index timer
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// Stop web service
		if (this.webServiceApp != null) {
			this.webServiceApp.stop();
			this.webServiceApp = null;
		}
	}

}
