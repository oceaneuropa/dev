package org.orbit.infra.runtime.datacast.ws;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.api.util.InfraClients;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.datacast.service.DataCastService;
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

public class DataCastServiceAdapter implements LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(DataCastServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<DataCastService, DataCastService> serviceTracker;
	protected DataCastWSApplication webApp;
	protected ServiceIndexTimer<DataCastService> indexTimer;
	protected ExtensibleServiceEditPolicy editPolicy;

	/**
	 * 
	 * @param properties
	 */
	public DataCastServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
		if (this.properties == null) {
			this.properties = new HashMap<Object, Object>();
		}
	}

	public IndexServiceClient getIndexProvider() {
		return InfraClients.getInstance().getIndexService(this.properties, true);
	}

	public DataCastService getService() {
		return (this.serviceTracker != null) ? this.serviceTracker.getService() : null;
	}

	@Override
	public void start(final BundleContext bundleContext) {
		LOG.debug("start()");

		this.serviceTracker = new ServiceTracker<DataCastService, DataCastService>(bundleContext, DataCastService.class, new ServiceTrackerCustomizer<DataCastService, DataCastService>() {
			@Override
			public DataCastService addingService(ServiceReference<DataCastService> reference) {
				DataCastService service = bundleContext.getService(reference);
				LOG.debug("ServiceTracker DataCastService [" + service + "] is added.");

				doStart(bundleContext, service);

				return service;
			}

			@Override
			public void modifiedService(ServiceReference<DataCastService> reference, DataCastService service) {
				LOG.debug("ServiceTracker DataCastService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<DataCastService> reference, DataCastService service) {
				LOG.debug("ServiceTracker DataCastService [" + service + "] is removed.");

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

	protected void doStart(BundleContext bundleContext, DataCastService service) {
		// Install edit policies
		this.editPolicy = new ExtensibleServiceEditPolicy(InfraConstants.DATACAST__EDITPOLICY_ID, DataCastService.class, InfraConstants.DATACAST__SERVICE_NAME);
		ServiceEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstall(this.editPolicy.getId());
		editPolicies.install(this.editPolicy);

		// Start web service
		LOG.debug("start web service");
		this.webApp = new DataCastWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO);
		this.webApp.start(bundleContext);

		// Start index timer
		LOG.debug("start index timer");
		IndexServiceClient indexProvider = getIndexProvider();
		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, InfraConstants.IDX__DATACAST__INDEXER_ID);
		if (extension != null) {
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<DataCastService> indexTimerFactory = extension.createExecutableInstance(ServiceIndexTimerFactory.class);
			if (indexTimerFactory != null) {
				this.indexTimer = indexTimerFactory.create(indexProvider, service);
				if (this.indexTimer != null) {
					this.indexTimer.start();
				}
			}
		}
	}

	protected void doStop(BundleContext bundleContext, DataCastService service) {
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
