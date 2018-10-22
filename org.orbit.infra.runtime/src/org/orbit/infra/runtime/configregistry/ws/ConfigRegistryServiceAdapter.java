package org.orbit.infra.runtime.configregistry.ws;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.api.util.InfraClients;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.orbit.platform.sdk.util.ExtensibleServiceEditPolicy;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigRegistryServiceAdapter implements LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(ConfigRegistryServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<ConfigRegistryService, ConfigRegistryService> serviceTracker;
	protected ConfigRegistryWSApplication webApp;
	protected ServiceIndexTimer<ConfigRegistryService> indexTimer;
	protected ExtensibleServiceEditPolicy editPolicy;

	/**
	 * 
	 * @param properties
	 */
	public ConfigRegistryServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
		if (this.properties == null) {
			this.properties = new HashMap<Object, Object>();
		}
	}

	public IndexServiceClient getIndexProvider() {
		return InfraClients.getInstance().getIndexService(this.properties, true);
	}

	public ConfigRegistryService getService() {
		return (this.serviceTracker != null) ? this.serviceTracker.getService() : null;
	}

	@Override
	public void start(final BundleContext bundleContext) {
		LOG.debug("start()");

		this.serviceTracker = new ServiceTracker<ConfigRegistryService, ConfigRegistryService>(bundleContext, ConfigRegistryService.class, new ServiceTrackerCustomizer<ConfigRegistryService, ConfigRegistryService>() {
			@Override
			public ConfigRegistryService addingService(ServiceReference<ConfigRegistryService> reference) {
				ConfigRegistryService service = bundleContext.getService(reference);
				LOG.debug("ServiceTracker ConfigRegistryService [" + service + "] is added.");

				doStart(bundleContext, service);

				return service;
			}

			@Override
			public void modifiedService(ServiceReference<ConfigRegistryService> reference, ConfigRegistryService service) {
				LOG.debug("ServiceTracker ConfigRegistryService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<ConfigRegistryService> reference, ConfigRegistryService service) {
				LOG.debug("ServiceTracker ConfigRegistryService [" + service + "] is removed.");

				doStop(bundleContext, service);
			}
		});
		this.serviceTracker.open();
	}

	@Override
	public void stop(final BundleContext bundleContext) {
		LOG.debug("stop()");

		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	protected void doStart(BundleContext bundleContext, ConfigRegistryService service) {
		// Install edit policies
		this.editPolicy = new ExtensibleServiceEditPolicy(InfraConstants.CONFIG_REGISTRY__EDITPOLICY_ID, ConfigRegistryService.class, InfraConstants.CONFIG_REGISTRY__SERVICE_NAME);
		ServiceEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstall(this.editPolicy.getId());
		editPolicies.install(this.editPolicy);

		// Start web service
		LOG.debug("start web service");
		this.webApp = new ConfigRegistryWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO);
		this.webApp.start(bundleContext);

		// Start index timer
		LOG.debug("start index timer");
		IndexServiceClient indexProvider = getIndexProvider();
		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, InfraConstants.IDX__CONFIG_REGISTRY__INDEXER_ID);
		if (extension != null) {
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

	protected void doStop(BundleContext bundleContext, ConfigRegistryService service) {
		// Stop index timer
		LOG.debug("stop index timer");
		if (this.indexTimer != null) {
			this.indexTimer.stop();
			this.indexTimer = null;
		}

		// Stop web service
		LOG.debug("stop web service");
		if (this.webApp != null) {
			this.webApp.stop(bundleContext);
			this.webApp = null;
		}

		// Uninstall edit policies
		if (this.editPolicy != null) {
			ServiceEditPolicies editPolicies = service.getEditPolicies();
			editPolicies.uninstall(this.editPolicy.getId());
			this.editPolicy = null;
		}
	}

}
