package org.orbit.component.runtime.tier3.nodecontrol.ws.other;

import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.component.runtime.tier3.nodecontrol.ws.TransferAgentServiceTimer;
import org.orbit.component.runtime.tier3.nodecontrol.ws.TransferAgentWSApplication;
import org.orbit.component.runtime.tier3.nodecontrol.ws.editpolicy.NodeWSEditPolicy;
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
	protected ServiceTracker<NodeControlService, NodeControlService> serviceTracker;
	protected TransferAgentWSApplication webService;
	protected TransferAgentServiceTimer serviceIndexTimer;

	public TransferAgentServiceAdapterV1(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public NodeControlService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<NodeControlService, NodeControlService>(bundleContext, NodeControlService.class, new ServiceTrackerCustomizer<NodeControlService, NodeControlService>() {
			@Override
			public NodeControlService addingService(ServiceReference<NodeControlService> reference) {
				NodeControlService service = bundleContext.getService(reference);
				LOG.info("TransferAgentService [" + service + "] is added.");

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<NodeControlService> reference, NodeControlService service) {
				LOG.info("TransferAgentService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<NodeControlService> reference, NodeControlService service) {
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
	protected void doStart(BundleContext bundleContext, NodeControlService service) {
		// Install web service edit policies
		WSEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstallEditPolicy(NodeWSEditPolicy.ID); // ensure NodeWSEditPolicy instance is not duplicated
		editPolicies.installEditPolicy(new NodeWSEditPolicy());

		// Start web service
		this.webService = new TransferAgentWSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webService.start(bundleContext);

		// Start index timer
		IndexProvider indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		this.serviceIndexTimer = new TransferAgentServiceTimer(indexProvider, service);
		this.serviceIndexTimer.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, NodeControlService service) {
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
