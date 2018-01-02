package org.orbit.component.runtime.tier1.session.ws;

import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier1.session.service.OAuth2Service;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.IndexProviderLoadBalancer;
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
	protected OAuth2WSApplication webServiceApp;
	protected OAuth2ServiceIndexTimer serviceIndexTimer;

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
				// stopWebService(bundleContext, service);
				// startWebService(bundleContext, service);
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
		// Start web service
		this.webServiceApp = new OAuth2WSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webServiceApp.start(bundleContext);

		// Start index timer
		IndexProvider indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		this.serviceIndexTimer = new OAuth2ServiceIndexTimer(indexProvider, service);
		this.serviceIndexTimer.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void stopWebService(BundleContext bundleContext, OAuth2Service service) {
		// Stop index timer
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// Stop web service
		if (this.webServiceApp != null) {
			this.webServiceApp.stop(bundleContext);
			this.webServiceApp = null;
		}
	}

}
