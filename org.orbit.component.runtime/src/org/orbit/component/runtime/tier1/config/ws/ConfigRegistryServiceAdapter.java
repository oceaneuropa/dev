package org.orbit.component.runtime.tier1.config.ws;

import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Adapter to start ConfigRegistryWSApplication when ConfigRegistryService becomes available and to stop ConfigRegistryWSApplication when ConfigRegistryService
 * becomes unavailable.
 * 
 */
public class ConfigRegistryServiceAdapter {

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<ConfigRegistryService, ConfigRegistryService> serviceTracker;
	protected ConfigRegistryWSApplication webServiceApp;
	protected ConfigRegistryServiceIndexTimer serviceIndexTimer;

	public ConfigRegistryServiceAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public ConfigRegistryService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<ConfigRegistryService, ConfigRegistryService>(bundleContext, ConfigRegistryService.class, new ServiceTrackerCustomizer<ConfigRegistryService, ConfigRegistryService>() {
			@Override
			public ConfigRegistryService addingService(ServiceReference<ConfigRegistryService> reference) {
				ConfigRegistryService service = bundleContext.getService(reference);
				System.out.println("ConfigRegistryService [" + service + "] is added.");

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<ConfigRegistryService> reference, ConfigRegistryService service) {
				System.out.println("ConfigRegistryService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<ConfigRegistryService> reference, ConfigRegistryService service) {
				System.out.println("ConfigRegistryService [" + service + "] is removed.");

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
	protected void doStart(BundleContext bundleContext, ConfigRegistryService service) {
		this.webServiceApp = new ConfigRegistryWSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.AUTHORIZATION_TOKEN_REQUEST_FILTER);
		this.webServiceApp.start(bundleContext);

		// Start a timer to update the indexing of the service
		IndexProvider indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		this.serviceIndexTimer = new ConfigRegistryServiceIndexTimer(indexProvider, service);
		this.serviceIndexTimer.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, ConfigRegistryService service) {
		// Stop Timers
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		if (this.webServiceApp != null) {
			this.webServiceApp.stop(bundleContext);
			this.webServiceApp = null;
		}
	}

}
