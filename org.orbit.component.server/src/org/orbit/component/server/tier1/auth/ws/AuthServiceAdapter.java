package org.orbit.component.server.tier1.auth.ws;

import org.orbit.component.server.tier1.auth.service.AuthService;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class AuthServiceAdapter {

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<AuthService, AuthService> serviceTracker;
	protected AuthWSApplication webApp;

	public AuthServiceAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public AuthService getService() {
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
		this.serviceTracker = new ServiceTracker<AuthService, AuthService>(bundleContext, AuthService.class, new ServiceTrackerCustomizer<AuthService, AuthService>() {
			@Override
			public AuthService addingService(ServiceReference<AuthService> reference) {
				AuthService service = bundleContext.getService(reference);
				System.out.println("AuthService [" + service + "] is added.");

				startWebService(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<AuthService> reference, AuthService service) {
				System.out.println("AuthService [" + service + "] is modified.");
				// stopWebService(bundleContext, service);
				// startWebService(bundleContext, service);
			}

			@Override
			public void removedService(ServiceReference<AuthService> reference, AuthService service) {
				System.out.println("AuthService [" + service + "] is removed.");

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
	protected void startWebService(BundleContext bundleContext, AuthService service) {
		this.webApp = new AuthWSApplication(bundleContext, service);
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
	protected void stopWebService(BundleContext bundleContext, AuthService service) {
		if (this.webApp != null) {
			this.webApp.stop();
			this.webApp = null;
		}
	}

}
