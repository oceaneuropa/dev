package other.orbit.component.runtime.tier4.missioncontrol.ws;

import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlService;
import org.orbit.component.runtime.tier4.missioncontrol.ws.MissionControlIndexTimer;
import org.orbit.component.runtime.tier4.missioncontrol.ws.MissionControlWSApplication;
import org.orbit.infra.api.indexes.IndexProviderClient;
import org.origin.common.rest.server.FeatureConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import other.orbit.infra.api.indexes.IndexProviderLoadBalancer;

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
	protected MissionControlIndexTimer serviceIndexer;

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
		this.webServiceApp = new MissionControlWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webServiceApp.start(bundleContext);

		// Start index timer
		IndexProviderClient indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		this.serviceIndexer = new MissionControlIndexTimer(indexProvider, service);
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
