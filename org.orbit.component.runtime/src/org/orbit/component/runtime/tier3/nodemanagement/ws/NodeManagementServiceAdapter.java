package org.orbit.component.runtime.tier3.nodemanagement.ws;

import java.util.Map;

import org.orbit.component.runtime.Extensions;
import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier3.nodemanagement.service.NodeManagementService;
import org.orbit.component.runtime.tier3.nodemanagement.ws.editpolicy.NodeWSEditPolicy;
import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.platform.sdk.extension.desc.ProgramExtension;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter to start NodeManagementWSApplication when NodeManagementService becomes available and to stop NodeManagementWSApplication when NodeManagementService
 * becomes unavailable.
 * 
 */
public class NodeManagementServiceAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(NodeManagementServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<NodeManagementService, NodeManagementService> serviceTracker;
	protected NodeManagementWSApplication webApp;
	protected NodeManagementServiceTimer indexTimer;
	protected ProgramExtension urlProviderExtension;

	public NodeManagementServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexProvider getIndexProvider() {
		return InfraClients.getInstance().getIndexProviderProxy(this.properties);
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
				LOG.info("NodeManagementService [" + service + "] is added.");

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<NodeManagementService> reference, NodeManagementService service) {
				LOG.info("NodeManagementService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<NodeManagementService> reference, NodeManagementService service) {
				LOG.info("NodeManagementService [" + service + "] is removed.");

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

		// Start web app
		this.webApp = new NodeManagementWSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webApp.start(bundleContext);

		// Start indexing timer
		IndexProvider indexProvider = getIndexProvider();
		this.indexTimer = new NodeManagementServiceTimer(indexProvider, service);
		this.indexTimer.start();

		// Register URL provider extension
		// this.urlProviderExtension = new ProgramExtension(URLProvider.EXTENSION_TYPE_ID, Extensions.NODE_CONTROL_URL_PROVIDER_EXTENSION_ID);
		// this.urlProviderExtension.setName("Node control service URL provider");
		// this.urlProviderExtension.setDescription("Node control service URL provider description");
		// this.urlProviderExtension.addInterface(URLProvider.class, new URLProviderImpl(service));
		// Extensions.INSTANCE.addExtension(this.urlProviderExtension);
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, NodeManagementService service) {
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
