/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime.tier1.config.ws;

import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.api.util.InfraClients;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Adapter to start ConfigRegistryWSApplication when ConfigRegistryService becomes available and to stop ConfigRegistryWSApplication when ConfigRegistryService
 * becomes unavailable.
 * 
 */
public class ConfigRegistryServiceAdapter implements LifecycleAware {

	protected Map<Object, Object> properties;
	protected ServiceTracker<ConfigRegistryService, ConfigRegistryService> serviceTracker;
	protected ConfigRegistryWSApplication webApp;
	// protected ConfigRegistryServiceIndexTimer indexTimer;
	protected ServiceIndexTimer<ConfigRegistryService> indexTimer;

	public ConfigRegistryServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexServiceClient getIndexProvider() {
		return InfraClients.getInstance().getIndexService(this.properties, true);
	}

	public ConfigRegistryService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<ConfigRegistryService, ConfigRegistryService>(bundleContext, ConfigRegistryService.class, new ServiceTrackerCustomizer<ConfigRegistryService, ConfigRegistryService>() {
			@Override
			public ConfigRegistryService addingService(ServiceReference<ConfigRegistryService> reference) {
				ConfigRegistryService service = bundleContext.getService(reference);
				// System.out.println("ConfigRegistryService [" + service + "] is added.");

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<ConfigRegistryService> reference, ConfigRegistryService service) {
				// System.out.println("ConfigRegistryService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<ConfigRegistryService> reference, ConfigRegistryService service) {
				// System.out.println("ConfigRegistryService [" + service + "] is removed.");

				doStop(bundleContext, service);
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
	protected void doStart(BundleContext bundleContext, ConfigRegistryService service) {
		// Start web app
		this.webApp = new ConfigRegistryWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webApp.start(bundleContext);

		// Start indexing timer
		IndexServiceClient indexProvider = getIndexProvider();
		// this.indexTimer = new ConfigRegistryServiceIndexTimer(indexProvider, service);
		// this.indexTimer.start();

		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, ComponentConstants.CONFIG_REGISTRY_INDEXER_ID);
		if (extension != null) {
			// String indexProviderId = extension.getId();
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<ConfigRegistryService> indexTimerFactory = extension.createExecutableInstance(ServiceIndexTimerFactory.class);
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
	protected void doStop(BundleContext bundleContext, ConfigRegistryService service) {
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

// protected Extension urlProviderExtension;

// Unregister URL provider extension
// if (this.urlProviderExtension != null) {
// Extensions.INSTANCE.removeExtension(this.urlProviderExtension);
// this.urlProviderExtension = null;
// }

// Register URL provider extension
// this.urlProviderExtension = new ProgramExtension(URLProvider.EXTENSION_TYPE_ID, Extensions.CONFIG_REGISTRY_URL_PROVIDER_EXTENSION_ID);
// this.urlProviderExtension.setName("Config registry URL provider");
// this.urlProviderExtension.setDescription("Config registry URL provider description");
// this.urlProviderExtension.addInterface(URLProvider.class, new URLProviderImpl(service));
// Extensions.INSTANCE.addExtension(this.urlProviderExtension);
