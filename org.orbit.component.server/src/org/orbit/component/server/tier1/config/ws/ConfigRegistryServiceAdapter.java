package org.orbit.component.server.tier1.config.ws;

import org.orbit.component.server.tier1.config.service.ConfigRegistryService;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
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
	protected ConfigRegistryWSApplication webApp;

	public ConfigRegistryServiceAdapter() {
	}

	public ConfigRegistryServiceAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public ConfigRegistryService getService() {
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
		this.serviceTracker = new ServiceTracker<ConfigRegistryService, ConfigRegistryService>(bundleContext, ConfigRegistryService.class, new ServiceTrackerCustomizer<ConfigRegistryService, ConfigRegistryService>() {
			@Override
			public ConfigRegistryService addingService(ServiceReference<ConfigRegistryService> reference) {
				ConfigRegistryService service = bundleContext.getService(reference);
				System.out.println("ConfigRegistryService [" + service + "] is added.");

				startWebService(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<ConfigRegistryService> reference, ConfigRegistryService service) {
				System.out.println("ConfigRegistryService [" + service + "] is modified.");

				stopWebService(bundleContext, service);
				startWebService(bundleContext, service);
			}

			@Override
			public void removedService(ServiceReference<ConfigRegistryService> reference, ConfigRegistryService service) {
				System.out.println("ConfigRegistryService [" + service + "] is removed.");

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
	protected void startWebService(BundleContext bundleContext, ConfigRegistryService service) {
		this.webApp = new ConfigRegistryWSApplication();
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
	protected void stopWebService(BundleContext bundleContext, ConfigRegistryService service) {
		if (this.webApp != null) {
			this.webApp.stop();
			this.webApp = null;
		}
	}

}
