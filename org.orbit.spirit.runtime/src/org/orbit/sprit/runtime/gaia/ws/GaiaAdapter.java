package org.orbit.sprit.runtime.gaia.ws;

import java.util.Map;

import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.sdk.Activator;
import org.orbit.sprit.runtime.Constants;
import org.orbit.sprit.runtime.gaia.service.GAIA;
import org.orbit.sprit.runtime.gaia.ws.command.GaiaWSEditPolicy;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.common.rest.server.FeatureConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GaiaAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(GaiaAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<GAIA, GAIA> serviceTracker;
	protected GaiaWSApplication webServiceApp;
	// protected GaiaIndexTimer indexTimer;
	protected ServiceIndexTimer<GAIA> indexTimer;

	public GaiaAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexProvider getIndexProvider() {
		return InfraClients.getInstance().getIndexProviderProxy(this.properties);
	}

	public GAIA getService() {
		return (this.serviceTracker != null) ? this.serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<GAIA, GAIA>(bundleContext, GAIA.class, new ServiceTrackerCustomizer<GAIA, GAIA>() {
			@Override
			public GAIA addingService(ServiceReference<GAIA> reference) {
				GAIA gaia = bundleContext.getService(reference);
				doStart(bundleContext, gaia);
				return gaia;
			}

			@Override
			public void modifiedService(ServiceReference<GAIA> reference, GAIA gaia) {
			}

			@Override
			public void removedService(ServiceReference<GAIA> reference, GAIA gaia) {
				doStop(bundleContext, gaia);
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
	 * @param gaia
	 */
	protected void doStart(BundleContext bundleContext, GAIA gaia) {
		LOG.info("doStart()");

		// Install web service edit policies
		WSEditPolicies editPolicies = gaia.getEditPolicies();
		editPolicies.uninstallEditPolicy(GaiaWSEditPolicy.ID); // ensure GaiaWSEditPolicy instance is not duplicated
		editPolicies.installEditPolicy(new GaiaWSEditPolicy());

		// Start web service
		this.webServiceApp = new GaiaWSApplication(gaia, FeatureConstants.PING | FeatureConstants.JACKSON | FeatureConstants.MULTIPLEPART);
		this.webServiceApp.start(bundleContext);

		// Start index timer
		IndexProvider indexProvider = getIndexProvider();
		// this.indexTimer = new GaiaIndexTimer(indexProvider, gaia);
		// this.indexTimer.start();

		IExtension extension = Activator.getInstance().getExtensionRegistry().getExtension(InfraConstants.INDEX_PROVIDER_EXTENSION_TYPE_ID, Constants.GAIA_INDEXER_ID);
		if (extension != null) {
			// String indexProviderId = extension.getId();
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<GAIA> indexTimerFactory = extension.createExecutableInstance(ServiceIndexTimerFactory.class);
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
	protected void doStop(BundleContext bundleContext, GAIA gaia) {
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
		WSEditPolicies editPolicies = gaia.getEditPolicies();
		editPolicies.uninstallEditPolicy(GaiaWSEditPolicy.ID);
	}

}
