/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime.tier2.appstore.ws;

import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexProvider;
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

	protected Map<Object, Object> properties;
	protected ServiceTracker<AppStoreService, AppStoreService> serviceTracker;
	protected AppStoreWSApplication webApp;
	protected AppStoreServiceIndexTimer indexTimer;
	// protected Extension urlProviderExtension;

	public AppStoreServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexProvider getIndexProvider() {
		return InfraClients.getInstance().getIndexProviderProxy(this.properties);
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
		// LOG.info("doStart()");

		// Start web app
		this.webApp = new AppStoreWSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webApp.start(bundleContext);

		// Start indexing timer
		IndexProvider indexProvider = getIndexProvider();
		this.indexTimer = new AppStoreServiceIndexTimer(indexProvider, service);
		this.indexTimer.start();

		// Register URL provider extension
		// this.urlProviderExtension = new ProgramExtension(URLProvider.EXTENSION_TYPE_ID, Extensions.APP_STORE_URL_PROVIDER_EXTENSION_ID);
		// this.urlProviderExtension.setName("App store URL provider");
		// this.urlProviderExtension.setDescription("App store URL provider description");
		// this.urlProviderExtension.addInterface(URLProvider.class, new URLProviderImpl(service));
		// Extensions.INSTANCE.addExtension(this.urlProviderExtension);
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, AppStoreService service) {
		// LOG.info("doStop()");
		// Unregister URL provider extension
		// if (this.urlProviderExtension != null) {
		// Extensions.INSTANCE.removeExtension(this.urlProviderExtension);
		// this.urlProviderExtension = null;
		// }

		// Stop indexing timer
		if (this.indexTimer != null) {
			this.indexTimer.stop();
			this.indexTimer = null;
		}

		// Stop web app
		if (this.webApp != null) {
			this.webApp.stop(bundleContext);
			this.webApp = null;
		}
	}

}
