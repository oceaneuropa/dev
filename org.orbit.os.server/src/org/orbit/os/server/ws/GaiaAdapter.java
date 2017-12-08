package org.orbit.os.server.ws;

import org.orbit.os.server.service.GAIA;
import org.orbit.os.server.ws.editpolicy.GaiaWSEditPolicy;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.mgm.client.api.IndexProvider;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GaiaAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(GaiaAdapter.class);

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ServiceTracker<GAIA, GAIA> serviceTracker;
	protected GaiaWSApplication webServiceApp;
	protected GaiaIndexTimer serviceIndexTimer;

	public GaiaAdapter(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
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
				GAIA nodeOS = bundleContext.getService(reference);
				doStart(bundleContext, nodeOS);
				return nodeOS;
			}

			@Override
			public void modifiedService(ServiceReference<GAIA> reference, GAIA nodeOS) {
			}

			@Override
			public void removedService(ServiceReference<GAIA> reference, GAIA nodeOS) {
				doStop(bundleContext, nodeOS);
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
	 * @param nodeOS
	 */
	protected void doStart(BundleContext bundleContext, GAIA nodeOS) {
		LOG.info("doStart()");

		// Install web service edit policies
		WSEditPolicies editPolicies = nodeOS.getEditPolicies();
		editPolicies.uninstallEditPolicy(GaiaWSEditPolicy.ID); // ensure NodeOSWSEditPolicy instance is not duplicated
		editPolicies.installEditPolicy(new GaiaWSEditPolicy());

		// Start web service
		this.webServiceApp = new GaiaWSApplication(bundleContext, nodeOS);
		this.webServiceApp.start();

		// Start index timer
		IndexProvider indexProvider = this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
		this.serviceIndexTimer = new GaiaIndexTimer(indexProvider);
		this.serviceIndexTimer.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param nodeOS
	 */
	protected void doStop(BundleContext bundleContext, GAIA nodeOS) {
		LOG.info("doStop()");

		// Start index timer
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// Stop webService
		if (this.webServiceApp != null) {
			this.webServiceApp.stop();
			this.webServiceApp = null;
		}

		// Uninstall web service edit policies
		WSEditPolicies editPolicies = nodeOS.getEditPolicies();
		editPolicies.uninstallEditPolicy(GaiaWSEditPolicy.ID);
	}

}
