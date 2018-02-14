/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime.tier1.account.ws;

import java.util.Map;

import org.orbit.component.runtime.Extensions;
import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.platform.sdk.extension.util.ProgramExtension;
import org.orbit.platform.sdk.urlprovider.URLProvider;
import org.orbit.platform.sdk.urlprovider.URLProviderImpl;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter to start UserRegistryWSApplication when UserRegistryService becomes available and to stop UserRegistryWSApplication when UserRegistryService becomes
 * unavailable.
 * 
 */
public class UserRegistryServiceAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(UserRegistryServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<UserRegistryService, UserRegistryService> serviceTracker;
	protected UserRegistryWSApplication webApp;
	protected UserRegistryServiceIndexTimer indexTimer;
	protected ProgramExtension urlProviderExtension;

	public UserRegistryServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexProvider getIndexProvider() {
		return InfraClients.getInstance().getIndexProviderProxy(this.properties);
	}

	public UserRegistryService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<UserRegistryService, UserRegistryService>(bundleContext, UserRegistryService.class, new ServiceTrackerCustomizer<UserRegistryService, UserRegistryService>() {
			@Override
			public UserRegistryService addingService(ServiceReference<UserRegistryService> reference) {
				UserRegistryService service = bundleContext.getService(reference);
				System.out.println("UserRegistryService [" + service + "] is added.");

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<UserRegistryService> reference, UserRegistryService service) {
				System.out.println("UserRegistryService [" + service + "] is modified.");

				doStop(bundleContext, service);
				doStart(bundleContext, service);
			}

			@Override
			public void removedService(ServiceReference<UserRegistryService> reference, UserRegistryService service) {
				System.out.println("UserRegistryService [" + service + "] is removed.");

				doStop(bundleContext, service);
			}
		});
		serviceTracker.open();
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
	protected void doStart(BundleContext bundleContext, UserRegistryService service) {
		// Start ws app
		this.webApp = new UserRegistryWSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webApp.start(bundleContext);

		// Start indexing timer
		IndexProvider indexProvider = getIndexProvider();
		this.indexTimer = new UserRegistryServiceIndexTimer(indexProvider, service);
		this.indexTimer.start();

		// Register URL provider extension
		this.urlProviderExtension = new ProgramExtension(URLProvider.EXTENSION_TYPE_ID, Extensions.USER_REGISTRY_URL_PROVIDER_EXTENSION_ID);
		this.urlProviderExtension.setName("User registration URL provider");
		this.urlProviderExtension.setDescription("User registration URL provider description");
		this.urlProviderExtension.adapt(URLProvider.class, new URLProviderImpl(service));
		Extensions.INSTANCE.addExtension(this.urlProviderExtension);
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, UserRegistryService service) {
		// Unregister URL provider extension
		if (this.urlProviderExtension != null) {
			Extensions.INSTANCE.removeExtension(this.urlProviderExtension);
			this.urlProviderExtension = null;
		}

		// Stop indexing timer
		if (this.indexTimer != null) {
			this.indexTimer.stop();
			this.indexTimer = null;
		}

		// Stop ws app
		if (this.webApp != null) {
			this.webApp.stop(bundleContext);
			this.webApp = null;
		}
	}

}
