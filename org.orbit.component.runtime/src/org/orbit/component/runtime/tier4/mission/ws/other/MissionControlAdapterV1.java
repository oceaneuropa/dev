package org.orbit.component.runtime.tier4.mission.ws.other;

import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier4.mission.service.MissionControlService;
import org.orbit.component.runtime.tier4.mission.ws.MissionControlIndexer;
import org.orbit.component.runtime.tier4.mission.ws.MissionControlWSApplication;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.IndexProviderLoadBalancer;
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
public class MissionControlAdapterV1 {

	protected static Logger LOG = LoggerFactory.getLogger(MissionControlAdapterV1.class);

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<MissionControlService, MissionControlService> serviceTracker;
	protected MissionControlWSApplication webServiceApp;
	protected MissionControlIndexer serviceIndexer;

	public MissionControlAdapterV1(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
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
				LOG.info("MissionControl [" + service + "] is added.");

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<MissionControlService> reference, MissionControlService service) {
				LOG.info("MissionControl [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<MissionControlService> reference, MissionControlService service) {
				LOG.info("MissionControl [" + service + "] is removed.");

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

	protected void doStart(BundleContext bundleContext, MissionControlService service) {
		// Start web service
		this.webServiceApp = new MissionControlWSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webServiceApp.start(bundleContext);

		// Start index timer
		IndexProvider indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		this.serviceIndexer = new MissionControlIndexer(indexProvider, service);
		this.serviceIndexer.start();
	}

	protected void doStop(BundleContext bundleContext, MissionControlService service) {
		// Stop index timer
		if (this.serviceIndexer != null) {
			this.serviceIndexer.stop();
			this.serviceIndexer = null;
		}

		// Stop web service
		if (this.webServiceApp != null) {
			this.webServiceApp.stop(bundleContext);
			this.webServiceApp = null;
		}
	}

}
