package org.orbit.component.server.tier2.appstore.ws;

import org.orbit.component.server.tier2.appstore.service.AppStoreService;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Adapter to start AppStoreWSApplication when AppStoreService becomes available and to stop AppStoreWSApplication when AppStoreService becomes unavailable.
 * 
 */
public class AppStoreServiceAdapter {

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<AppStoreService, AppStoreService> serviceTracker;
	protected AppStoreWSApplication webApp;

	public AppStoreServiceAdapter() {
	}

	public AppStoreServiceAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public AppStoreService getService() {
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
		this.serviceTracker = new ServiceTracker<AppStoreService, AppStoreService>(bundleContext, AppStoreService.class, new ServiceTrackerCustomizer<AppStoreService, AppStoreService>() {
			@Override
			public AppStoreService addingService(ServiceReference<AppStoreService> reference) {
				AppStoreService service = bundleContext.getService(reference);
				System.out.println("AppStoreService [" + service + "] is added.");

				startWebService(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<AppStoreService> reference, AppStoreService service) {
				System.out.println("AppStoreService [" + service + "] is modified.");

				stopWebService(bundleContext, service);
				startWebService(bundleContext, service);
			}

			@Override
			public void removedService(ServiceReference<AppStoreService> reference, AppStoreService service) {
				System.out.println("AppStoreService [" + service + "] is removed.");

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
	protected void startWebService(BundleContext bundleContext, AppStoreService service) {
		this.webApp = new AppStoreWSApplication();
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
	protected void stopWebService(BundleContext bundleContext, AppStoreService service) {
		if (this.webApp != null) {
			this.webApp.stop();
			this.webApp = null;
		}
	}

}
