package org.orbit.spirit.runtime.gaia.ws;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.api.util.InfraClients;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.orbit.spirit.runtime.Constants;
import org.orbit.spirit.runtime.gaia.service.GaiaService;
import org.orbit.spirit.runtime.gaia.ws.command.GaiaEditPolicy;
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

public class GaiaAdapter implements LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(GaiaAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<GaiaService, GaiaService> serviceTracker;
	protected GaiaWSApplication webServiceApp;
	// protected GaiaIndexTimer indexTimer;
	protected ServiceIndexTimer<GaiaService> indexTimer;

	public GaiaAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexServiceClient getIndexProvider() {
		return InfraClients.getInstance().getIndexService(this.properties, true);
	}

	public GaiaService getService() {
		return (this.serviceTracker != null) ? this.serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<GaiaService, GaiaService>(bundleContext, GaiaService.class, new ServiceTrackerCustomizer<GaiaService, GaiaService>() {
			@Override
			public GaiaService addingService(ServiceReference<GaiaService> reference) {
				GaiaService gaia = bundleContext.getService(reference);
				doStart(bundleContext, gaia);
				return gaia;
			}

			@Override
			public void modifiedService(ServiceReference<GaiaService> reference, GaiaService gaia) {
			}

			@Override
			public void removedService(ServiceReference<GaiaService> reference, GaiaService gaia) {
				doStop(bundleContext, gaia);
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
	 * @param gaia
	 */
	protected void doStart(BundleContext bundleContext, GaiaService gaia) {
		LOG.info("doStart()");

		// Install web service edit policies
		ServiceEditPolicies editPolicies = gaia.getEditPolicies();
		editPolicies.uninstall(GaiaEditPolicy.ID); // ensure GaiaWSEditPolicy instance is not duplicated
		editPolicies.install(new GaiaEditPolicy());

		// Start web service
		this.webServiceApp = new GaiaWSApplication(gaia, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO | FeatureConstants.JACKSON | FeatureConstants.MULTIPLEPART);
		this.webServiceApp.start(bundleContext);

		// Start index timer
		IndexServiceClient indexProvider = getIndexProvider();
		// this.indexTimer = new GaiaIndexTimer(indexProvider, gaia);
		// this.indexTimer.start();

		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, Constants.IDX__GAIA__INDEXER_ID);
		if (extension != null) {
			// String indexProviderId = extension.getId();
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<GaiaService> indexTimerFactory = extension.createExecutableInstance(ServiceIndexTimerFactory.class);
			if (indexTimerFactory != null) {
				this.indexTimer = indexTimerFactory.create(indexProvider, gaia);
				if (this.indexTimer != null) {
					this.indexTimer.start();
				}
			}
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @param gaia
	 */
	protected void doStop(BundleContext bundleContext, GaiaService gaia) {
		LOG.info("doStop()");

		// Start index timer
		if (this.indexTimer != null) {
			this.indexTimer.stop();
			this.indexTimer = null;
		}

		// Stop webService
		if (this.webServiceApp != null) {
			this.webServiceApp.stop(bundleContext);
			this.webServiceApp = null;
		}

		// Uninstall web service edit policies
		ServiceEditPolicies editPolicies = gaia.getEditPolicies();
		editPolicies.uninstall(GaiaEditPolicy.ID);
	}

}
