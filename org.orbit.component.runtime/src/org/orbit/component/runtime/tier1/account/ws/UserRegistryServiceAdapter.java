package org.orbit.component.runtime.tier1.account.ws;

import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.IndexProviderLoadBalancer;
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
	protected UserRegistryServiceIndexTimer serviceIndexTimer;

	public UserRegistryServiceAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public UserRegistryService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
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

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<UserRegistryService> reference, UserRegistryService service) {
				System.out.println("UserRegistryService [" + service + "] is modified.");

				doStop(bundleContext, service);
				doStart(bundleContext, service);
			}

			@Override
			public void removedService(ServiceReference<UserRegistryService> reference, UserRegistryService service) {
				System.out.println("UserRegistryService [" + service + "] is removed.");

				doStop(bundleContext, service);
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
	protected void doStart(BundleContext bundleContext, UserRegistryService service) {
		this.webApp = new UserRegistryWSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.AUTHORIZATION_TOKEN_REQUEST_FILTER);
		this.webApp.start(bundleContext);

		// Start a timer to update the indexing of the service
		IndexProvider indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		this.serviceIndexTimer = new UserRegistryServiceIndexTimer(indexProvider, service);
		this.serviceIndexTimer.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, UserRegistryService service) {
		if (this.webApp != null) {
			this.webApp.stop(bundleContext);
			this.webApp = null;
		}
	}

}
