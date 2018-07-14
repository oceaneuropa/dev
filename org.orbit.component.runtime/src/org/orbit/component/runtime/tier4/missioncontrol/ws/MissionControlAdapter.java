package org.orbit.component.runtime.tier4.missioncontrol.ws;

import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlService;
import org.orbit.component.runtime.tier4.missioncontrol.ws.editpolicy.MissionWSEditPolicy;
import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.sdk.Activator;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter to start MissionControlWSApplication when MissionControl service becomes available and to stop MissionControlWSApplication when MissionControl
 * service becomes unavailable. The assumption is one MissionControl service per JVM.
 * 
 */
public class MissionControlAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(MissionControlAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<MissionControlService, MissionControlService> serviceTracker;
	protected MissionControlWSApplication webApp;
	// protected MissionControlIndexer indexTimer;
	protected ServiceIndexTimer<MissionControlService> indexTimer;

	public MissionControlAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexProvider getIndexProvider() {
		return InfraClients.getInstance().getIndexProviderProxy(this.properties);
	}

	public MissionControlService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<MissionControlService, MissionControlService>(bundleContext, MissionControlService.class, new ServiceTrackerCustomizer<MissionControlService, MissionControlService>() {
			@Override
			public MissionControlService addingService(ServiceReference<MissionControlService> reference) {
				MissionControlService service = bundleContext.getService(reference);
				// LOG.info("MissionControl [" + service + "] is added.");

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<MissionControlService> reference, MissionControlService service) {
				// LOG.info("MissionControl [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<MissionControlService> reference, MissionControlService service) {
				// LOG.info("MissionControl [" + service + "] is removed.");

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

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStart(BundleContext bundleContext, MissionControlService service) {
		// Install web service edit policies
		WSEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstallEditPolicy(MissionWSEditPolicy.ID); // ensure MissionWSEditPolicy instance is not duplicated
		editPolicies.installEditPolicy(new MissionWSEditPolicy());

		// Start web app
		this.webApp = new MissionControlWSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webApp.start(bundleContext);

		// Start indexing timer
		IndexProvider indexProvider = getIndexProvider();
		// this.indexTimer = new MissionControlIndexer(indexProvider, service);
		// this.indexTimer.start();

		IExtension extension = Activator.getInstance().getExtensionRegistry().getExtension(InfraConstants.INDEX_PROVIDER_EXTENSION_TYPE_ID, OrbitConstants.MISSION_CONTROL_INDEXER_ID);
		if (extension != null) {
			// String indexProviderId = extension.getId();
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<MissionControlService> indexTimerFactory = extension.createExecutableInstance(ServiceIndexTimerFactory.class);
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
	protected void doStop(BundleContext bundleContext, MissionControlService service) {
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

		// Uninstall web service edit policies
		WSEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstallEditPolicy(MissionWSEditPolicy.ID);
	}

}

// protected Extension urlProviderExtension;

// Register URL provider extension
// this.urlProviderExtension = new ProgramExtension(URLProvider.EXTENSION_TYPE_ID, Extensions.MISSION_CONTROL_URL_PROVIDER_EXTENSION_ID);
// this.urlProviderExtension.setName("Mission control service URL provider");
// this.urlProviderExtension.setDescription("Mission control service URL provider description");
// this.urlProviderExtension.addInterface(URLProvider.class, new URLProviderImpl(service));
// Extensions.INSTANCE.addExtension(this.urlProviderExtension);

// Unregister URL provider extension
// if (this.urlProviderExtension != null) {
// Extensions.INSTANCE.removeExtension(this.urlProviderExtension);
// this.urlProviderExtension = null;
// }
