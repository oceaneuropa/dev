package org.orbit.component.runtime.tier3.domainmanagement.ws;

import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.extensions.Extensions;
import org.orbit.component.runtime.tier3.domainmanagement.service.DomainManagementService;
import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.platform.sdk.extension.desc.ProgramExtension;
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
	protected DomainServiceTimer indexTimer;
	protected ProgramExtension urlProviderExtension;

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
				LOG.info("DomainService [" + service + "] is added.");

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<DomainManagementService> reference, DomainManagementService service) {
				LOG.info("DomainService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<DomainManagementService> reference, DomainManagementService service) {
				LOG.info("DomainService [" + service + "] is removed.");

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
		this.indexTimer = new DomainServiceTimer(indexProvider, service);
		this.indexTimer.start();

		// Register URL provider extension
		// this.urlProviderExtension = new ProgramExtension(URLProvider.EXTENSION_TYPE_ID, Extensions.DOMAIN_SERVICE_URL_PROVIDER_EXTENSION_ID);
		// this.urlProviderExtension.setName("Domain management service URL provider");
		// this.urlProviderExtension.setDescription("Domain management service URL provider description");
		// this.urlProviderExtension.addInterface(URLProvider.class, new URLProviderImpl(service));
		// Extensions.INSTANCE.addExtension(this.urlProviderExtension);
	}

	protected void doStop(BundleContext bundleContext, DomainManagementService service) {
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

		// Stop web app
		if (this.webApp != null) {
			this.webApp.stop(bundleContext);
			this.webApp = null;
		}
	}

}
