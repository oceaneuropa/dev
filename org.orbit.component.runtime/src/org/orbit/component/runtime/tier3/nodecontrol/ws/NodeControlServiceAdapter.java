package org.orbit.component.runtime.tier3.nodecontrol.ws;

import java.util.Map;

import org.orbit.component.runtime.Extensions;
import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.component.runtime.tier3.nodecontrol.ws.editpolicy.NodeWSEditPolicy;
import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.platform.sdk.extension.util.ProgramExtension;
import org.orbit.platform.sdk.urlprovider.URLProvider;
import org.orbit.platform.sdk.urlprovider.URLProviderImpl;
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
public class NodeControlServiceAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(NodeControlServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<NodeControlService, NodeControlService> serviceTracker;
	protected TransferAgentWSApplication webApp;
	protected TransferAgentServiceTimer indexTimer;
	protected ProgramExtension urlProviderExtension;

	public NodeControlServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexProvider getIndexProvider() {
		return InfraClients.getInstance().getIndexProviderProxy(this.properties);
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

		// Start web app
		this.webApp = new TransferAgentWSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webApp.start(bundleContext);

		// Start indexing timer
		IndexProvider indexProvider = getIndexProvider();
		this.indexTimer = new TransferAgentServiceTimer(indexProvider, service);
		this.indexTimer.start();

		// Register URL provider extension
		this.urlProviderExtension = new ProgramExtension(URLProvider.EXTENSION_TYPE_ID, Extensions.NODE_CONTROL_URL_PROVIDER_EXTENSION_ID);
		this.urlProviderExtension.setName("Node control service URL provider");
		this.urlProviderExtension.setDescription("Node control service URL provider description");
		this.urlProviderExtension.adapt(URLProvider.class, new URLProviderImpl(service));
		Extensions.INSTANCE.addExtension(this.urlProviderExtension);
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, NodeControlService service) {
		// Unregister URL provider extension
		if (this.urlProviderExtension != null) {
			Extensions.INSTANCE.removeExtension(this.urlProviderExtension);
			this.urlProviderExtension = null;
		}

		// Stop indexing timer
		if (this.indexTimer != null) {
			this.indexTimer.stop();
			this.indexTimer = null;
		}

		// Stop web app
		if (this.webApp != null) {
			this.webApp.stop(bundleContext);
			this.webApp = null;
		}

		// Uninstall web service edit policies
		WSEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstallEditPolicy(NodeWSEditPolicy.ID);
	}

}
