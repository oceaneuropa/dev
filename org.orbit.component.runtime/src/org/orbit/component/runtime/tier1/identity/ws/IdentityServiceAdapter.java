package org.orbit.component.runtime.tier1.identity.ws;

import java.util.Map;

import org.orbit.component.runtime.ComponentsConstants;
import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier1.identity.service.IdentityService;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.api.util.InfraClients;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdentityServiceAdapter implements LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(IdentityServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<IdentityService, IdentityService> serviceTracker;
	protected IdentityServiceWSApplication webApp;
	protected ServiceIndexTimer<IdentityService> indexTimer;

	public IdentityServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexProvider getIndexProvider() {
		return InfraClients.getInstance().getIndexProvider(this.properties, true);
	}

	public IdentityService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<IdentityService, IdentityService>(bundleContext, IdentityService.class, new ServiceTrackerCustomizer<IdentityService, IdentityService>() {
			@Override
			public IdentityService addingService(ServiceReference<IdentityService> reference) {
				IdentityService service = bundleContext.getService(reference);
				// System.out.println("IdentityService [" + service + "] is added.");

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<IdentityService> reference, IdentityService service) {
				// System.out.println("IdentityService [" + service + "] is modified.");

				doStop(bundleContext, service);
				doStart(bundleContext, service);
			}

			@Override
			public void removedService(ServiceReference<IdentityService> reference, IdentityService service) {
				// System.out.println("IdentityService [" + service + "] is removed.");

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
	protected void doStart(BundleContext bundleContext, IdentityService service) {
		// Start ws app
		this.webApp = new IdentityServiceWSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.NAME | OrbitFeatureConstants.ECHO);
		this.webApp.start(bundleContext);

		// Start indexing timer
		IndexProvider indexProvider = getIndexProvider();

		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, ComponentsConstants.IDENTITY_INDEXER_ID);
		if (extension != null) {
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<IdentityService> indexTimerFactory = extension.createExecutableInstance(ServiceIndexTimerFactory.class);
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
	protected void doStop(BundleContext bundleContext, IdentityService service) {
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
