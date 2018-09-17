package org.orbit.spirit.runtime.earth.ws;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.api.util.InfraClients;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.orbit.platform.sdk.util.ExtensibleServiceEditPolicy;
import org.orbit.spirit.runtime.SpiritConstants;
import org.orbit.spirit.runtime.earth.service.EarthService;
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

public class EarthServiceAdapter implements LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(EarthServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<EarthService, EarthService> serviceTracker;
	protected EarthWSApplication webServiceApp;
	protected ServiceIndexTimer<EarthService> indexTimer;
	protected ExtensibleServiceEditPolicy editPolicy;

	public EarthServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexServiceClient getIndexProvider() {
		return InfraClients.getInstance().getIndexService(this.properties, true);
	}

	public EarthService getService() {
		return (this.serviceTracker != null) ? this.serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<EarthService, EarthService>(bundleContext, EarthService.class, new ServiceTrackerCustomizer<EarthService, EarthService>() {
			@Override
			public EarthService addingService(ServiceReference<EarthService> reference) {
				EarthService earth = bundleContext.getService(reference);
				doStart(bundleContext, earth);
				return earth;
			}

			@Override
			public void modifiedService(ServiceReference<EarthService> reference, EarthService gaia) {
			}

			@Override
			public void removedService(ServiceReference<EarthService> reference, EarthService gaia) {
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
	 * @param earth
	 */
	protected void doStart(BundleContext bundleContext, EarthService earth) {
		LOG.info("doStart()");

		// Install edit policies
		this.editPolicy = new ExtensibleServiceEditPolicy(SpiritConstants.EARTH__EDITPOLICY_ID, EarthService.class, SpiritConstants.EARTH__SERVICE_NAME);
		ServiceEditPolicies editPolicies = earth.getEditPolicies();
		editPolicies.uninstall(this.editPolicy.getId());
		editPolicies.install(this.editPolicy);

		// Start web service
		this.webServiceApp = new EarthWSApplication(earth, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO | FeatureConstants.JACKSON | FeatureConstants.MULTIPLEPART);
		this.webServiceApp.start(bundleContext);

		// Start index timer
		IndexServiceClient indexProvider = getIndexProvider();
		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, SpiritConstants.IDX__EARTH__INDEXER_ID);
		if (extension != null) {
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<EarthService> indexTimerFactory = extension.createExecutableInstance(ServiceIndexTimerFactory.class);
			if (indexTimerFactory != null) {
				this.indexTimer = indexTimerFactory.create(indexProvider, earth);
				if (this.indexTimer != null) {
					this.indexTimer.start();
				}
			}
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @param earth
	 */
	protected void doStop(BundleContext bundleContext, EarthService earth) {
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

		// Uninstall edit policies
		if (this.editPolicy != null) {
			ServiceEditPolicies editPolicies = earth.getEditPolicies();
			editPolicies.uninstall(this.editPolicy.getId());
			this.editPolicy = null;
		}
	}

}
