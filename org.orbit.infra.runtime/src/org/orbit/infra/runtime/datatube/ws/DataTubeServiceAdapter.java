package org.orbit.infra.runtime.datatube.ws;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.api.util.InfraClients;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.datatube.service.DataTubeService;
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

public class DataTubeServiceAdapter implements LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(DataTubeServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<DataTubeService, DataTubeService> serviceTracker;
	protected DataTubeWebSocketHandler webSocketHandler;
	protected DataTubeWSApplication webApp;
	protected ServiceIndexTimer<DataTubeService> indexTimer;
	protected ExtensibleServiceEditPolicy editPolicy;

	/**
	 * 
	 * @param properties
	 */
	public DataTubeServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
		if (this.properties == null) {
			this.properties = new HashMap<Object, Object>();
		}
	}

	public IndexServiceClient getIndexProvider() {
		return InfraClients.getInstance().getIndexService(this.properties, true);
	}

	public DataTubeService getService() {
		return (this.serviceTracker != null) ? this.serviceTracker.getService() : null;
	}

	@Override
	public void start(final BundleContext bundleContext) {
		LOG.debug("start()");

		this.serviceTracker = new ServiceTracker<DataTubeService, DataTubeService>(bundleContext, DataTubeService.class, new ServiceTrackerCustomizer<DataTubeService, DataTubeService>() {
			@Override
			public DataTubeService addingService(ServiceReference<DataTubeService> reference) {
				DataTubeService service = bundleContext.getService(reference);
				LOG.debug("ServiceTracker DataTubeService [" + service + "] is added.");

				doStart(bundleContext, service);

				return service;
			}

			@Override
			public void modifiedService(ServiceReference<DataTubeService> reference, DataTubeService service) {
				LOG.debug("ServiceTracker DataTubeService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<DataTubeService> reference, DataTubeService service) {
				LOG.debug("ServiceTracker DataTubeService [" + service + "] is removed.");

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

	protected void doStart(BundleContext bundleContext, DataTubeService service) {
		// Install edit policies
		this.editPolicy = new ExtensibleServiceEditPolicy(InfraConstants.DATATUBE__EDITPOLICY_ID, DataTubeService.class, InfraConstants.DATATUBE__SERVICE_NAME);
		ServiceEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstall(this.editPolicy.getId());
		editPolicies.install(this.editPolicy);

		// Start web socket
		LOG.debug("start web socket");
		this.webSocketHandler = new DataTubeWebSocketHandler(service);
		this.webSocketHandler.start(bundleContext);

		// Start web service
		LOG.debug("start web service");
		this.webApp = new DataTubeWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO);
		this.webApp.start(bundleContext);

		// Start index timer
		LOG.debug("start index timer");
		IndexServiceClient indexProvider = getIndexProvider();
		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, InfraConstants.IDX__DATATUBE__INDEXER_ID);
		if (extension != null) {
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<DataTubeService> indexTimerFactory = extension.createExecutableInstance(ServiceIndexTimerFactory.class);
			if (indexTimerFactory != null) {
				this.indexTimer = indexTimerFactory.create(indexProvider, service);
				if (this.indexTimer != null) {
					this.indexTimer.start();
				}
			}
		}
	}

	protected void doStop(BundleContext bundleContext, DataTubeService service) {
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

		// Stop web socket
		LOG.debug("stop web socket");
		if (this.webSocketHandler != null) {
			this.webSocketHandler.stop(bundleContext);
			this.webSocketHandler = null;
		}

		// Uninstall edit policies
		if (this.editPolicy != null) {
			ServiceEditPolicies editPolicies = service.getEditPolicies();
			editPolicies.uninstall(this.editPolicy.getId());
			this.editPolicy = null;
		}
	}

}
