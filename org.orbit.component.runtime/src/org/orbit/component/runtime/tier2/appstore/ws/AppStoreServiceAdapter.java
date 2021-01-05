package org.orbit.component.runtime.tier2.appstore.ws;

import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.service.ILifecycle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter to start AppStoreWSApplication when AppStoreService becomes available and to stop AppStoreWSApplication when AppStoreService becomes unavailable.
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class AppStoreServiceAdapter implements ILifecycle {

	protected static Logger LOG = LoggerFactory.getLogger(AppStoreServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<AppStoreService, AppStoreService> serviceTracker;
	protected AppStoreWSApplication webApp;
	protected ServiceIndexTimer<AppStoreService> indexTimer;

	/**
	 * 
	 * @param properties
	 */
	public AppStoreServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public AppStoreService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/** ILifecycle */
	@Override
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<AppStoreService, AppStoreService>(bundleContext, AppStoreService.class, new ServiceTrackerCustomizer<AppStoreService, AppStoreService>() {
			@Override
			public AppStoreService addingService(ServiceReference<AppStoreService> reference) {
				AppStoreService service = bundleContext.getService(reference);
				doStop(bundleContext, service);
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
	protected void doStart(BundleContext bundleContext, AppStoreService service) {
		this.webApp = new AppStoreWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webApp.start(bundleContext);

		this.indexTimer = new AppStoreServiceIndexTimer(service);
		this.indexTimer.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, AppStoreService service) {
		if (this.indexTimer != null) {
			this.indexTimer.stop();
			this.indexTimer = null;
		}

		if (this.webApp != null) {
			this.webApp.stop(bundleContext);
			this.webApp = null;
		}
	}

}

/*-
// Impl - 1
// Start indexing timer
// IndexServiceClient indexProvider = getIndexProvider();
// this.indexTimer = new AppStoreServiceIndexTimer(indexProvider, service);
// this.indexTimer.start();
*/

/*-
// Impl - 2
IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, ComponentConstants.APP_STORE_INDEXER_ID);
if (extension != null) {
	// String indexProviderId = extension.getId();
	@SuppressWarnings("unchecked")
	ServiceIndexTimerFactory<AppStoreService> indexTimerFactory = extension.createInstance(ServiceIndexTimerFactory.class);
	if (indexTimerFactory != null) {
		this.indexTimer = indexTimerFactory.create(service);
		if (this.indexTimer != null) {
			this.indexTimer.start();
		}
	}
}
*/

// protected AppStoreServiceIndexTimer indexTimer;

// public IndexServiceClient getIndexProvider() {
// return InfraClients.getInstance().getIndexService(this.properties, true);
// }

// protected Extension urlProviderExtension;

// Register URL provider extension
// this.urlProviderExtension = new ProgramExtension(URLProvider.EXTENSION_TYPE_ID, Extensions.APP_STORE_URL_PROVIDER_EXTENSION_ID);
// this.urlProviderExtension.setName("App store URL provider");
// this.urlProviderExtension.setDescription("App store URL provider description");
// this.urlProviderExtension.addInterface(URLProvider.class, new URLProviderImpl(service));
// Extensions.INSTANCE.addExtension(this.urlProviderExtension);

// LOG.info("doStop()");
// Unregister URL provider extension
// if (this.urlProviderExtension != null) {
// Extensions.INSTANCE.removeExtension(this.urlProviderExtension);
// this.urlProviderExtension = null;
// }
