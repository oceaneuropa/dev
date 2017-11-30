package org.orbit.component.server.tier1.auth.ws;

import org.orbit.component.server.tier1.auth.service.AuthService;
import org.origin.mgm.client.api.IndexProvider;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class AuthServiceAdapter {

	protected ServiceTracker<AuthService, AuthService> serviceTracker;
	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected IndexProvider indexProvider;
	protected AuthWSApplication webApp;
	protected AuthServiceIndexTimer serviceIndexTimer;

	public AuthServiceAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public AuthService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
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

				doStart(bundleContext, service);

				return service;
			}

			@Override
			public void modifiedService(ServiceReference<AuthService> reference, AuthService service) {
				System.out.println("AuthService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<AuthService> reference, AuthService service) {
				System.out.println("AuthService [" + service + "] is removed.");

				doStop(bundleContext, service);
			}
		});
		this.serviceTracker.open();

		this.indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
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

	protected void doStart(BundleContext bundleContext, AuthService service) {
		this.webApp = new AuthWSApplication(bundleContext, service);
		this.webApp.start();

		this.serviceIndexTimer = new AuthServiceIndexTimer(this.indexProvider, service);
		this.serviceIndexTimer.start();
	}

	protected void doStop(BundleContext bundleContext, AuthService service) {
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		if (this.webApp != null) {
			this.webApp.stop();
			this.webApp = null;
		}
	}

}
