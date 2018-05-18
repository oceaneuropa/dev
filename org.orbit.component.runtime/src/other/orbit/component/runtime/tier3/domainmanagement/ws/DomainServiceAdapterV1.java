package other.orbit.component.runtime.tier3.domainmanagement.ws;

import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier3.domainmanagement.service.DomainManagementService;
import org.orbit.component.runtime.tier3.domainmanagement.ws.DomainServiceTimer;
import org.orbit.component.runtime.tier3.domainmanagement.ws.DomainServiceWSApplication;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter to start DomainMgmtWSApplication when DomainMgmtService becomes available and to stop DomainMgmtWSApplication when DomainMgmtService becomes
 * unavailable.
 * 
 */
public class DomainServiceAdapterV1 {

	protected static Logger LOG = LoggerFactory.getLogger(DomainServiceAdapterV1.class);

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<DomainManagementService, DomainManagementService> serviceTracker;
	protected DomainServiceWSApplication webServiceApp;
	protected DomainServiceTimer serviceIndexTimer;

	public DomainServiceAdapterV1(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

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
		this.webServiceApp = new DomainServiceWSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webServiceApp.start(bundleContext);

		// Start index timer
		IndexProvider indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		this.serviceIndexTimer = new DomainServiceTimer(indexProvider, service);
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
