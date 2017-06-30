package org.orbit.component.server.tier3.domain.ws;

import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Adapter to start DomainMgmtWSApplication when DomainMgmtService becomes available and to stop DomainMgmtWSApplication when DomainMgmtService becomes
 * unavailable.
 * 
 */
public class DomainMgmtServiceAdapter {

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<DomainManagementService, DomainManagementService> serviceTracker;
	protected DomainMgmtWSApplication webApp;

	public DomainMgmtServiceAdapter() {
	}

	public DomainMgmtServiceAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public DomainManagementService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	public IndexProviderLoadBalancer getIndexProviderLoadBalancer() {
		return indexProviderLoadBalancer;
	}

	public void setIndexProviderLoadBalancer(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
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
				System.out.println("DomainMgmtService [" + service + "] is added.");

				startDomainMgmtWebService(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<DomainManagementService> reference, DomainManagementService service) {
				System.out.println("DomainMgmtService [" + service + "] is modified.");

				stopDomainMgmtWebService(bundleContext, service);
				startDomainMgmtWebService(bundleContext, service);
			}

			@Override
			public void removedService(ServiceReference<DomainManagementService> reference, DomainManagementService service) {
				System.out.println("DomainMgmtService [" + service + "] is removed.");

				stopDomainMgmtWebService(bundleContext, service);
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

	protected void startDomainMgmtWebService(BundleContext bundleContext, DomainManagementService service) {
		this.webApp = new DomainMgmtWSApplication();
		this.webApp.setBundleContext(bundleContext);
		this.webApp.setContextRoot(service.getContextRoot());
		if (this.indexProviderLoadBalancer != null) {
			this.webApp.setIndexProvider(this.indexProviderLoadBalancer.createLoadBalancableIndexProvider());
		}
		this.webApp.start();
	}

	protected void stopDomainMgmtWebService(BundleContext bundleContext, DomainManagementService service) {
		if (this.webApp != null) {
			this.webApp.stop();
			this.webApp = null;
		}
	}

}
