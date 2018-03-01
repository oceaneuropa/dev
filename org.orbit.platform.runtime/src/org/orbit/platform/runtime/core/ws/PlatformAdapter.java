package org.orbit.platform.runtime.core.ws;

import java.util.Map;

import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.platform.runtime.core.Platform;
import org.orbit.platform.runtime.core.command.PlatformWSEditPolicy;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.common.rest.server.FeatureConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlatformAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(PlatformAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<Platform, Platform> serviceTracker;
	protected PlatformWSApplication wsApp;
	protected PlatformIndexTimer indexTimer;

	public PlatformAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexProvider getIndexProvider() {
		return InfraClients.getInstance().getIndexProviderProxy(this.properties);
	}

	public Platform getService() {
		return (this.serviceTracker != null) ? this.serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<Platform, Platform>(bundleContext, Platform.class, new ServiceTrackerCustomizer<Platform, Platform>() {
			@Override
			public Platform addingService(ServiceReference<Platform> reference) {
				Platform platform = bundleContext.getService(reference);
				doStart(bundleContext, platform);
				return platform;
			}

			@Override
			public void modifiedService(ServiceReference<Platform> reference, Platform gaia) {
			}

			@Override
			public void removedService(ServiceReference<Platform> reference, Platform gaia) {
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
	 * @param service
	 */
	protected void doStart(BundleContext bundleContext, Platform service) {
		LOG.info("doStart()");

		// Install edit policies
		WSEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.installEditPolicy(new PlatformWSEditPolicy());

		// Start web service
		this.wsApp = new PlatformWSApplication(service, FeatureConstants.PING | FeatureConstants.JACKSON | FeatureConstants.MULTIPLEPART);
		this.wsApp.start(bundleContext);

		// Start index timer
		IndexProvider indexProvider = getIndexProvider();
		this.indexTimer = new PlatformIndexTimer(indexProvider, service);
		this.indexTimer.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param platform
	 */
	protected void doStop(BundleContext bundleContext, Platform platform) {
		LOG.info("doStop()");

		// Start index timer
		if (this.indexTimer != null) {
			this.indexTimer.stop();
			this.indexTimer = null;
		}

		// Stop webService
		if (this.wsApp != null) {
			this.wsApp.stop(bundleContext);
			this.wsApp = null;
		}

		// Uninstall edit policies
		WSEditPolicies editPolicies = platform.getEditPolicies();
		editPolicies.uninstallEditPolicy(PlatformWSEditPolicy.ID);
	}

}
