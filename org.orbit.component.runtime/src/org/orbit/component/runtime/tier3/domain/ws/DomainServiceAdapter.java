package org.orbit.component.runtime.tier3.domain.ws;

import java.util.Map;

import org.orbit.component.runtime.OrbitConstants;
import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.origin.common.extensions.core.IExtension;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter to start DomainMgmtWSApplication when DomainMgmtService becomes available and to stop DomainMgmtWSApplication when DomainMgmtService becomes
 * unavailable.
 * 
 */
public class DomainServiceAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(DomainServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<DomainManagementService, DomainManagementService> serviceTracker;
	protected DomainServiceWSApplication webApp;
	// protected DomainServiceTimer indexTimer;
	protected ServiceIndexTimer<DomainManagementService> indexTimer;

	public DomainServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public DomainManagementService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	public IndexProvider getIndexProvider() {
		return InfraClients.getInstance().getIndexProviderProxy(this.properties);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<DomainManagementService, DomainManagementService>(bundleContext, DomainManagementService.class, new ServiceTrackerCustomizer<DomainManagementService, DomainManagementService>() {
			@Override
			public DomainManagementService addingService(ServiceReference<DomainManagementService> reference) {
				DomainManagementService service = bundleContext.getService(reference);
				// LOG.info("DomainService [" + service + "] is added.");

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<DomainManagementService> reference, DomainManagementService service) {
				// LOG.info("DomainService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<DomainManagementService> reference, DomainManagementService service) {
				// LOG.info("DomainService [" + service + "] is removed.");

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

	protected void doStart(BundleContext bundleContext, DomainManagementService service) {
		// Start web app
		this.webApp = new DomainServiceWSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webApp.start(bundleContext);

		// Start indexing timer
		IndexProvider indexProvider = getIndexProvider();
		// this.indexTimer = new DomainServiceTimer(indexProvider, service);
		// this.indexTimer.start();

		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, OrbitConstants.DOMAIN_SERVICE_INDEXER_ID);
		if (extension != null) {
			// String indexProviderId = extension.getId();
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<DomainManagementService> indexTimerFactory = extension.createExecutableInstance(ServiceIndexTimerFactory.class);
			if (indexTimerFactory != null) {
				this.indexTimer = indexTimerFactory.create(indexProvider, service);
				if (this.indexTimer != null) {
					this.indexTimer.start();
				}
			}
		}
	}

	protected void doStop(BundleContext bundleContext, DomainManagementService service) {
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

// Register URL provider extension
// this.urlProviderExtension = new ProgramExtension(URLProvider.EXTENSION_TYPE_ID, Extensions.DOMAIN_SERVICE_URL_PROVIDER_EXTENSION_ID);
// this.urlProviderExtension.setName("Domain management service URL provider");
// this.urlProviderExtension.setDescription("Domain management service URL provider description");
// this.urlProviderExtension.addInterface(URLProvider.class, new URLProviderImpl(service));
// Extensions.INSTANCE.addExtension(this.urlProviderExtension);

// Unregister URL provider extension
// if (this.urlProviderExtension != null) {
// Extensions.INSTANCE.removeExtension(this.urlProviderExtension);
// this.urlProviderExtension = null;
// }