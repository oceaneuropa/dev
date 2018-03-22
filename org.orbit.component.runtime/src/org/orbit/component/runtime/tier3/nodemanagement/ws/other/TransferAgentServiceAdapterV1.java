package org.orbit.component.runtime.tier3.nodemanagement.ws.other;

import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier3.nodemanagement.service.NodeManagementService;
import org.orbit.component.runtime.tier3.nodemanagement.ws.NodeManagementServiceTimer;
import org.orbit.component.runtime.tier3.nodemanagement.ws.NodeManagementWSApplication;
import org.orbit.component.runtime.tier3.nodemanagement.ws.editpolicy.NodeWSEditPolicy;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.IndexProviderLoadBalancer;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter to start TransferAgentWSApplication when TransferAgentService becomes available and to stop TransferAgentWSApplication when TransferAgentService
 * becomes unavailable.
 * 
 */
public class TransferAgentServiceAdapterV1 {

	protected static Logger LOG = LoggerFactory.getLogger(TransferAgentServiceAdapterV1.class);

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<NodeManagementService, NodeManagementService> serviceTracker;
	protected NodeManagementWSApplication webService;
	protected NodeManagementServiceTimer serviceIndexTimer;

	public TransferAgentServiceAdapterV1(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public NodeManagementService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<NodeManagementService, NodeManagementService>(bundleContext, NodeManagementService.class, new ServiceTrackerCustomizer<NodeManagementService, NodeManagementService>() {
			@Override
			public NodeManagementService addingService(ServiceReference<NodeManagementService> reference) {
				NodeManagementService service = bundleContext.getService(reference);
				LOG.info("TransferAgentService [" + service + "] is added.");

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<NodeManagementService> reference, NodeManagementService service) {
				LOG.info("TransferAgentService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<NodeManagementService> reference, NodeManagementService service) {
				LOG.info("TransferAgentService [" + service + "] is removed.");

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
	protected void doStart(BundleContext bundleContext, NodeManagementService service) {
		// Install web service edit policies
		WSEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstallEditPolicy(NodeWSEditPolicy.ID); // ensure NodeWSEditPolicy instance is not duplicated
		editPolicies.installEditPolicy(new NodeWSEditPolicy());

		// Start web service
		this.webService = new NodeManagementWSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webService.start(bundleContext);

		// Start index timer
		IndexProvider indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		this.serviceIndexTimer = new NodeManagementServiceTimer(indexProvider, service);
		this.serviceIndexTimer.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, NodeManagementService service) {
		// Stop index timer
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// Stop web service
		if (this.webService != null) {
			this.webService.stop(bundleContext);
			this.webService = null;
		}

		// Uninstall web service edit policies
		WSEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstallEditPolicy(NodeWSEditPolicy.ID);
	}

}
