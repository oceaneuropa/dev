package org.orbit.component.runtime.tier1.auth.ws;

import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.tier1.auth.service.AuthService;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.service.ILifecycle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class AuthServiceAdapter implements ILifecycle {

	protected Map<Object, Object> properties;
	protected ServiceTracker<AuthService, AuthService> serviceTracker;
	protected AuthWSApplication webApp;
	protected ServiceIndexTimer<AuthService> indexTimer;

	/**
	 * 
	 * @param properties
	 */
	public AuthServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public AuthService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/** ILifecycle */
	@Override
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<AuthService, AuthService>(bundleContext, AuthService.class, new ServiceTrackerCustomizer<AuthService, AuthService>() {
			@Override
			public AuthService addingService(ServiceReference<AuthService> reference) {
				AuthService service = bundleContext.getService(reference);
				// System.out.println("AuthService [" + service + "] is added.");

				doStart(bundleContext, service);

				return service;
			}

			@Override
			public void modifiedService(ServiceReference<AuthService> reference, AuthService service) {
				// System.out.println("AuthService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<AuthService> reference, AuthService service) {
				// System.out.println("AuthService [" + service + "] is removed.");

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
	protected void doStart(BundleContext bundleContext, AuthService service) {
		// Start web app
		this.webApp = new AuthWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO);
		this.webApp.start(bundleContext);

		// Start indexing timer
		// IndexServiceClient indexProvider = getIndexProvider();
		// this.indexTimer = new AuthServiceIndexTimer(indexProvider, service);
		// this.indexTimer.start();

		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, ComponentConstants.AUTH_INDEXER_ID);
		if (extension != null) {
			// String indexProviderId = extension.getId();
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<AuthService> indexTimerFactory = extension.createInstance(ServiceIndexTimerFactory.class);
			if (indexTimerFactory != null) {
				this.indexTimer = indexTimerFactory.create(service);
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
	protected void doStop(BundleContext bundleContext, AuthService service) {
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

// protected AuthServiceIndexTimer indexTimer;
// public IndexServiceClient getIndexProvider() {
// return InfraClients.getInstance().getIndexService(this.properties, true);
// }

// protected Extension urlProviderExtension;

// Register URL provider extension
// this.urlProviderExtension = new ProgramExtension(URLProvider.EXTENSION_TYPE_ID, Extensions.AUTH_URL_PROVIDER_EXTENSION_ID);
// this.urlProviderExtension.setName("Auth URL provider");
// this.urlProviderExtension.setDescription("Auth URL provider description");
// this.urlProviderExtension.addInterface(URLProvider.class, new URLProviderImpl(service));
// Extensions.INSTANCE.addExtension(this.urlProviderExtension);

// Unregister URL provider extension
// if (this.urlProviderExtension != null) {
// Extensions.INSTANCE.removeExtension(this.urlProviderExtension);
// this.urlProviderExtension = null;
// }
