package org.orbit.infra.runtime.extensionregistry.ws;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionRegistryService;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtensionRegistryAdapter implements LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(ExtensionRegistryAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<ExtensionRegistryService, ExtensionRegistryService> serviceTracker;
	protected ExtensionRegistryWSApplication webService;
	protected ServiceIndexTimer<ExtensionRegistryService> indexTimer;

	/**
	 * 
	 * @param properties
	 */
	public ExtensionRegistryAdapter(Map<Object, Object> properties) {
		this.properties = checkProperties(properties);
	}

	protected Map<Object, Object> checkProperties(Map<Object, Object> properties) {
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		return properties;
	}

	public ExtensionRegistryService getService() {
		return (this.serviceTracker != null) ? this.serviceTracker.getService() : null;
	}

	// public IndexServiceClient getIndexProvider() {
	// return InfraClients.getInstance().getIndexService(this.properties, true);
	// }

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void start(final BundleContext bundleContext) {
		LOG.debug("start()");

		this.serviceTracker = new ServiceTracker<ExtensionRegistryService, ExtensionRegistryService>(bundleContext, ExtensionRegistryService.class, new ServiceTrackerCustomizer<ExtensionRegistryService, ExtensionRegistryService>() {
			@Override
			public ExtensionRegistryService addingService(ServiceReference<ExtensionRegistryService> reference) {
				ExtensionRegistryService service = bundleContext.getService(reference);
				LOG.debug("ServiceTracker ExtensionRegistryService [" + service + "] is added.");

				doStart(bundleContext, service);

				return service;
			}

			@Override
			public void modifiedService(ServiceReference<ExtensionRegistryService> reference, ExtensionRegistryService service) {
				LOG.debug("ServiceTracker ExtensionRegistryService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<ExtensionRegistryService> reference, ExtensionRegistryService service) {
				LOG.debug("ServiceTracker ExtensionRegistryService [" + service + "] is removed.");

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
	public void stop(final BundleContext bundleContext) {
		LOG.debug("stop()");

		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	protected void doStart(BundleContext bundleContext, ExtensionRegistryService service) {
		// 1. start web service
		LOG.debug("start web service");
		this.webService = new ExtensionRegistryWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO);
		this.webService.start(bundleContext);

		// 2. start index timer
		LOG.debug("start index timer");
		// IndexServiceClient indexProvider = getIndexProvider();
		// this.indexTimer = new ExtensionRegistryServiceIndexTimer(indexProvider, service);
		// this.indexTimer.start();

		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, InfraConstants.EXTENSION_REGISTRY_INDEXER_ID);
		if (extension != null) {
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<ExtensionRegistryService> indexTimerFactory = extension.createInstance(ServiceIndexTimerFactory.class);
			if (indexTimerFactory != null) {
				this.indexTimer = indexTimerFactory.create(service);
				if (this.indexTimer != null) {
					this.indexTimer.start();
				}
			}
		}
	}

	protected void doStop(BundleContext bundleContext, ExtensionRegistryService service) {
		// 1. stop index timer
		LOG.debug("stop index timer");
		if (this.indexTimer != null) {
			this.indexTimer.stop();
			this.indexTimer = null;
		}

		// 2. stop web service
		LOG.debug("stop web service");
		if (this.webService != null) {
			this.webService.stop(bundleContext);
			this.webService = null;
		}
	}

}
