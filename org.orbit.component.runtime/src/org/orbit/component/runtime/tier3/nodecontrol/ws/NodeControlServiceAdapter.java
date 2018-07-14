package org.orbit.component.runtime.tier3.nodecontrol.ws;

import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.NodeControlWSEditPolicy;
import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.sdk.Activator;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter to start NodeControlWSApplication when NodeControlService becomes available and to stop NodeControlWSApplication when NodeControlService becomes
 * unavailable.
 * 
 */
public class NodeControlServiceAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(NodeControlServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<NodeControlService, NodeControlService> serviceTracker;
	protected NodeControlWSApplication webApp;
	// protected NodeControlServiceTimer indexTimer;
	protected ServiceIndexTimer<NodeControlService> indexTimer;

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
				// LOG.info("NodeControlService [" + service + "] is added.");

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<NodeControlService> reference, NodeControlService service) {
				// LOG.info("NodeControlService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<NodeControlService> reference, NodeControlService service) {
				// LOG.info("NodeControlService [" + service + "] is removed.");

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
		editPolicies.uninstallEditPolicy(NodeControlWSEditPolicy.ID); // ensure NodeWSEditPolicy instance is not duplicated
		editPolicies.installEditPolicy(new NodeControlWSEditPolicy());

		// Start web app
		this.webApp = new NodeControlWSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webApp.start(bundleContext);

		// Start indexing timer
		IndexProvider indexProvider = getIndexProvider();
		// this.indexTimer = new NodeControlServiceTimer(indexProvider, service);
		// this.indexTimer.start();

		IExtension extension = Activator.getInstance().getExtensionRegistry().getExtension(InfraConstants.INDEX_PROVIDER_EXTENSION_TYPE_ID, OrbitConstants.NODE_CONTROL_INDEXER_ID);
		if (extension != null) {
			// String indexProviderId = extension.getId();
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<NodeControlService> indexTimerFactory = extension.createExecutableInstance(ServiceIndexTimerFactory.class);
			if (indexTimerFactory != null) {
				this.indexTimer = indexTimerFactory.create(indexProvider, service);
				if (this.indexTimer != null) {
					this.indexTimer.start();
				}
			}
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, NodeControlService service) {
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
		editPolicies.uninstallEditPolicy(NodeControlWSEditPolicy.ID);
	}

}

// protected Extension urlProviderExtension;

// Register URL provider extension
// this.urlProviderExtension = new ProgramExtension(URLProvider.EXTENSION_TYPE_ID, Extensions.NODE_CONTROL_URL_PROVIDER_EXTENSION_ID);
// this.urlProviderExtension.setName("Node control service URL provider");
// this.urlProviderExtension.setDescription("Node control service URL provider description");
// this.urlProviderExtension.addInterface(URLProvider.class, new URLProviderImpl(service));
// Extensions.INSTANCE.addExtension(this.urlProviderExtension);

// Unregister URL provider extension
// if (this.urlProviderExtension != null) {
// Extensions.INSTANCE.removeExtension(this.urlProviderExtension);
// this.urlProviderExtension = null;
// }
