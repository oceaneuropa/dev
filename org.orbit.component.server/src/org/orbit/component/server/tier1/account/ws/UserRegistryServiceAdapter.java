package org.orbit.component.server.tier1.account.ws;

import org.orbit.component.server.tier1.account.service.UserRegistryService;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Adapter to start UserRegistryWSApplication when UserRegistryService becomes available and to stop UserRegistryWSApplication when UserRegistryService becomes
 * unavailable.
 * 
 */
public class UserRegistryServiceAdapter {

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<UserRegistryService, UserRegistryService> serviceTracker;
	protected UserRegistryWSApplication webApp;

	public UserRegistryServiceAdapter() {
	}

	public UserRegistryServiceAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public UserRegistryService getService() {
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
		this.serviceTracker = new ServiceTracker<UserRegistryService, UserRegistryService>(bundleContext, UserRegistryService.class, new ServiceTrackerCustomizer<UserRegistryService, UserRegistryService>() {
			@Override
			public UserRegistryService addingService(ServiceReference<UserRegistryService> reference) {
				UserRegistryService service = bundleContext.getService(reference);
				System.out.println("UserRegistryService [" + service + "] is added.");

				startWebService(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<UserRegistryService> reference, UserRegistryService service) {
				System.out.println("UserRegistryService [" + service + "] is modified.");

				stopWebService(bundleContext, service);
				startWebService(bundleContext, service);
			}

			@Override
			public void removedService(ServiceReference<UserRegistryService> reference, UserRegistryService service) {
				System.out.println("UserRegistryService [" + service + "] is removed.");

				stopWebService(bundleContext, service);
			}
		});
		serviceTracker.open();
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
	protected void startWebService(BundleContext bundleContext, UserRegistryService service) {
		this.webApp = new UserRegistryWSApplication(bundleContext, service);
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
	protected void stopWebService(BundleContext bundleContext, UserRegistryService service) {
		if (this.webApp != null) {
			this.webApp.stop();
			this.webApp = null;
		}
	}

}
