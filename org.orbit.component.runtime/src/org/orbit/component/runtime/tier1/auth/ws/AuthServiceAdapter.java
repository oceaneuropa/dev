package org.orbit.component.runtime.tier1.auth.ws;

import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier1.auth.service.AuthService;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class AuthServiceAdapter {

	protected ServiceTracker<AuthService, AuthService> serviceTracker;
	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected AuthWSApplication webService;
	protected AuthServiceIndexTimer indexTimer;

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
		// Start web service
		this.webService = new AuthWSApplication(service, OrbitFeatureConstants.PING);
		this.webService.start(bundleContext);

		// Start indexing
		IndexProvider indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		this.indexTimer = new AuthServiceIndexTimer(indexProvider, service);
		this.indexTimer.start();
	}

	protected void doStop(BundleContext bundleContext, AuthService service) {
		// Stop indexing
		if (this.indexTimer != null) {
			this.indexTimer.stop();
			this.indexTimer = null;
		}

		// Stop web service
		if (this.webService != null) {
			this.webService.stop(bundleContext);
			this.webService = null;
		}
	}

}
