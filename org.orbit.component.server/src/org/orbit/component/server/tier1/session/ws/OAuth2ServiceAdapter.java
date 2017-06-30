package org.orbit.component.server.tier1.session.ws;

import org.orbit.component.server.tier1.session.service.OAuth2Service;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Adapter to start OAuth2WSApplication when OAuth2Service becomes available and to stop OAuth2WSApplication when OAuth2Service becomes unavailable.
 * 
 */
public class OAuth2ServiceAdapter {

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<OAuth2Service, OAuth2Service> serviceTracker;
	protected OAuth2WSApplication webApp;

	public OAuth2ServiceAdapter() {
	}

	public OAuth2ServiceAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public OAuth2Service getService() {
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
		this.serviceTracker = new ServiceTracker<OAuth2Service, OAuth2Service>(bundleContext, OAuth2Service.class, new ServiceTrackerCustomizer<OAuth2Service, OAuth2Service>() {
			@Override
			public OAuth2Service addingService(ServiceReference<OAuth2Service> reference) {
				OAuth2Service service = bundleContext.getService(reference);
				System.out.println("OAuth2Service [" + service + "] is added.");

				startWebService(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<OAuth2Service> reference, OAuth2Service service) {
				System.out.println("OAuth2Service [" + service + "] is modified.");

				stopWebService(bundleContext, service);
				startWebService(bundleContext, service);
			}

			@Override
			public void removedService(ServiceReference<OAuth2Service> reference, OAuth2Service service) {
				System.out.println("OAuth2Service [" + service + "] is removed.");

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
	protected void startWebService(BundleContext bundleContext, OAuth2Service service) {
		this.webApp = new OAuth2WSApplication();
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
	protected void stopWebService(BundleContext bundleContext, OAuth2Service service) {
		if (this.webApp != null) {
			this.webApp.stop();
			this.webApp = null;
		}
	}

}
