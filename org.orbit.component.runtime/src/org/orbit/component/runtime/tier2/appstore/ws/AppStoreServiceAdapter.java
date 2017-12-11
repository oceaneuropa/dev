package org.orbit.component.runtime.tier2.appstore.ws;

import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter to start AppStoreWSApplication when AppStoreService becomes available and to stop AppStoreWSApplication when AppStoreService becomes unavailable.
 * 
 */
public class AppStoreServiceAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(AppStoreServiceAdapter.class);

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<AppStoreService, AppStoreService> serviceTracker;
	protected AppStoreWSApplication webServiceApp;
	protected AppStoreServiceIndexTimer serviceIndexTimer;

	public AppStoreServiceAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public AppStoreService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
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
				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<AppStoreService> reference, AppStoreService service) {
			}

			@Override
			public void removedService(ServiceReference<AppStoreService> reference, AppStoreService service) {
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
	protected void doStart(BundleContext bundleContext, AppStoreService service) {
		LOG.info("doStart()");

		// Start web service
		this.webServiceApp = new AppStoreWSApplication(bundleContext, service);
		this.webServiceApp.start();

		// Start index timer
		IndexProvider indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		this.serviceIndexTimer = new AppStoreServiceIndexTimer(indexProvider, service);
		this.serviceIndexTimer.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, AppStoreService service) {
		LOG.info("doStop()");

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
