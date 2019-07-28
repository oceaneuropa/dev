package other.orbit.component.runtime.tier1.config.ws;

import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceV0;
import org.orbit.component.runtime.tier1.config.ws.ConfigRegistryServiceIndexTimer;
import org.orbit.component.runtime.tier1.config.ws.ConfigRegistryWSApplication;
import org.origin.common.rest.server.FeatureConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

// import other.orbit.infra.api.indexes.IndexProviderLoadBalancer;

/**
 * Adapter to start ConfigRegistryWSApplication when ConfigRegistryService becomes available and to stop ConfigRegistryWSApplication when ConfigRegistryService
 * becomes unavailable.
 * 
 */
public class ConfigRegistryServiceAdapterV1 {

	// protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<ConfigRegistryServiceV0, ConfigRegistryServiceV0> serviceTracker;
	protected ConfigRegistryWSApplication webServiceApp;
	protected ConfigRegistryServiceIndexTimer serviceIndexTimer;

//	public ConfigRegistryServiceAdapterV1(IndexProviderLoadBalancer indexProviderLoadBalancer) {
//		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
//	}

	public ConfigRegistryServiceV0 getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<ConfigRegistryServiceV0, ConfigRegistryServiceV0>(bundleContext, ConfigRegistryServiceV0.class, new ServiceTrackerCustomizer<ConfigRegistryServiceV0, ConfigRegistryServiceV0>() {
			@Override
			public ConfigRegistryServiceV0 addingService(ServiceReference<ConfigRegistryServiceV0> reference) {
				ConfigRegistryServiceV0 service = bundleContext.getService(reference);
				System.out.println("ConfigRegistryService [" + service + "] is added.");

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<ConfigRegistryServiceV0> reference, ConfigRegistryServiceV0 service) {
				System.out.println("ConfigRegistryService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<ConfigRegistryServiceV0> reference, ConfigRegistryServiceV0 service) {
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
	protected void doStart(BundleContext bundleContext, ConfigRegistryServiceV0 service) {
		this.webServiceApp = new ConfigRegistryWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webServiceApp.start(bundleContext);

		// Start a timer to update the indexing of the service
		// IndexProviderClient indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		this.serviceIndexTimer = new ConfigRegistryServiceIndexTimer(service);
		this.serviceIndexTimer.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, ConfigRegistryServiceV0 service) {
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
