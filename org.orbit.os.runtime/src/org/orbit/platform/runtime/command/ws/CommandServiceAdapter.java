package org.orbit.platform.runtime.command.ws;

import java.util.Map;

import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.platform.runtime.command.service.CommandService;
import org.orbit.platform.runtime.command.ws.command.CommandServiceWSEditPolicy;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.common.rest.server.FeatureConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandServiceAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(CommandServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<CommandService, CommandService> serviceTracker;
	protected CommandServiceWSApplication webServiceApp;
	protected CommandServiceIndexTimer serviceIndexTimer;

	public CommandServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexProvider getIndexProvider() {
		return InfraClients.getInstance().getIndexProviderProxy(this.properties);
	}

	public CommandService getService() {
		return (this.serviceTracker != null) ? this.serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<CommandService, CommandService>(bundleContext, CommandService.class, new ServiceTrackerCustomizer<CommandService, CommandService>() {
			@Override
			public CommandService addingService(ServiceReference<CommandService> reference) {
				CommandService service = bundleContext.getService(reference);
				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<CommandService> reference, CommandService service) {
			}

			@Override
			public void removedService(ServiceReference<CommandService> reference, CommandService service) {
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
	protected void doStart(BundleContext bundleContext, CommandService service) {
		LOG.info("doStart()");

		// Install web service edit policies
		WSEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstallEditPolicy(CommandServiceWSEditPolicy.ID); // ensure CommandServiceWSEditPolicy instance is not duplicated
		editPolicies.installEditPolicy(new CommandServiceWSEditPolicy());

		// Start web service
		this.webServiceApp = new CommandServiceWSApplication(service, FeatureConstants.PING | FeatureConstants.JACKSON);
		this.webServiceApp.start(bundleContext);

		// Start index timer
		IndexProvider indexProvider = getIndexProvider();
		this.serviceIndexTimer = new CommandServiceIndexTimer(indexProvider, service);
		this.serviceIndexTimer.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, CommandService service) {
		LOG.info("doStop()");

		// Start index timer
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// Stop webService
		if (this.webServiceApp != null) {
			this.webServiceApp.stop(bundleContext);
			this.webServiceApp = null;
		}

		// Uninstall web service edit policies
		WSEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstallEditPolicy(CommandServiceWSEditPolicy.ID);
	}

}
