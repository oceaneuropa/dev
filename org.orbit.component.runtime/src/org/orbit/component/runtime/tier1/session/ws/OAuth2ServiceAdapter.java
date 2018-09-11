package org.orbit.component.runtime.tier1.session.ws;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier1.session.service.OAuth2Service;
import org.orbit.infra.api.indexes.IndexProviderClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import other.orbit.infra.api.indexes.IndexProviderLoadBalancer;

/**
 * Adapter to start OAuth2WSApplication when OAuth2Service becomes available and to stop OAuth2WSApplication when OAuth2Service becomes unavailable.
 * 
 */
public class OAuth2ServiceAdapter implements LifecycleAware {

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<OAuth2Service, OAuth2Service> serviceTracker;
	protected OAuth2WSApplication webServiceApp;
	// protected OAuth2ServiceIndexTimer indexTimer;
	protected ServiceIndexTimer<OAuth2Service> indexTimer;

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
	@Override
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
	@Override
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
		this.webServiceApp = new OAuth2WSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webServiceApp.start(bundleContext);

		// Start index timer
		IndexProviderClient indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		// this.indexTimer = new OAuth2ServiceIndexTimer(indexProvider, service);
		// this.indexTimer.start();

		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, ComponentConstants.OAUTH2_INDEXER_ID);
		if (extension != null) {
			// String indexProviderId = extension.getId();
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<OAuth2Service> indexTimerFactory = extension.createExecutableInstance(ServiceIndexTimerFactory.class);
			if (indexTimerFactory != null) {
				this.indexTimer = indexTimerFactory.create(indexProvider, service);
				if (this.indexTimer != null) {
					this.indexTimer.start();
				}
			}
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void stopWebService(BundleContext bundleContext, OAuth2Service service) {
		// Stop index timer
		if (this.indexTimer != null) {
			this.indexTimer.stop();
			this.indexTimer = null;
		}

		// Stop web service
		if (this.webServiceApp != null) {
			this.webServiceApp.stop(bundleContext);
			this.webServiceApp = null;
		}
	}

}
