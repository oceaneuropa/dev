package other.orbit.component.runtime.tier3.domainmanagement.ws;

import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.orbit.component.runtime.tier3.domain.ws.DomainServiceTimer;
import org.orbit.component.runtime.tier3.domain.ws.DomainServiceWSApplication;
import org.origin.common.rest.server.FeatureConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import other.orbit.infra.api.indexes.IndexProviderLoadBalancer;

/**
 * Adapter to start DomainMgmtWSApplication when DomainMgmtService becomes available and to stop DomainMgmtWSApplication when DomainMgmtService becomes
 * unavailable.
 * 
 */
public class DomainServiceAdapterV1 {

	protected static Logger LOG = LoggerFactory.getLogger(DomainServiceAdapterV1.class);

	// protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<DomainManagementService, DomainManagementService> serviceTracker;
	protected DomainServiceWSApplication webServiceApp;
	protected DomainServiceTimer serviceIndexTimer;

//	public DomainServiceAdapterV1(IndexProviderLoadBalancer indexProviderLoadBalancer) {
//		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
//	}

	public DomainManagementService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<DomainManagementService, DomainManagementService>(bundleContext, DomainManagementService.class, new ServiceTrackerCustomizer<DomainManagementService, DomainManagementService>() {
			@Override
			public DomainManagementService addingService(ServiceReference<DomainManagementService> reference) {
				DomainManagementService service = bundleContext.getService(reference);
				LOG.info("DomainMgmtService [" + service + "] is added.");

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<DomainManagementService> reference, DomainManagementService service) {
				LOG.info("DomainMgmtService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<DomainManagementService> reference, DomainManagementService service) {
				LOG.info("DomainMgmtService [" + service + "] is removed.");

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

	protected void doStart(BundleContext bundleContext, DomainManagementService service) {
		// Start web service
		this.webServiceApp = new DomainServiceWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webServiceApp.start(bundleContext);

		// Start index timer
		// IndexProviderClient indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		this.serviceIndexTimer = new DomainServiceTimer(service);
		this.serviceIndexTimer.start();
	}

	protected void doStop(BundleContext bundleContext, DomainManagementService service) {
		// Stop index timer
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// Stop web service
		if (this.webServiceApp != null) {
			this.webServiceApp.stop(bundleContext);
			this.webServiceApp = null;
		}
	}

}
