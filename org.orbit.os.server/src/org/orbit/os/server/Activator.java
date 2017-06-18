package org.orbit.os.server;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.orbit.os.server.cli.NodeOSCommand;
import org.orbit.os.server.service.NodeOS;
import org.orbit.os.server.util.SetupUtil;
import org.orbit.os.server.ws.NodeOSWSApplication;
import org.origin.common.util.PropertyUtil;
import org.origin.mgm.client.api.IndexServiceUtil;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class Activator implements BundleActivator {

	protected static BundleContext bundleContext;
	protected static ServiceTracker<NodeOS, NodeOS> nodeOSTracker;

	static BundleContext getContext() {
		return bundleContext;
	}

	public static NodeOS getNodeOS() {
		return (nodeOSTracker != null) ? nodeOSTracker.getService() : null;
	}

	public static Properties getNodeHomeConfigIniProperties() {
		Properties configIniProps = SetupUtil.getNodeHomeConfigIniProperties(bundleContext);
		return configIniProps;
	}

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected NodeOSCommand nodeOSCommand;
	protected NodeOSWSApplication nodeOSWebApp;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.bundleContext = bundleContext;

		// -----------------------------------------------------------------------------
		// Get load balancer for IndexProvider
		// -----------------------------------------------------------------------------
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		// (1) load config.ini properties first
		Properties configIniProps = Activator.getNodeHomeConfigIniProperties();
		@SuppressWarnings("unchecked")
		Enumeration<String> enumr = ((Enumeration<String>) configIniProps.propertyNames());
		while (enumr.hasMoreElements()) {
			String propName = (String) enumr.nextElement();
			String propValue = configIniProps.getProperty(propName);
			if (propName != null && propValue != null) {
				indexProviderProps.put(propName, propValue);
			}
		}
		// (2) bundle/system/env properties takes precedence over config.ini properties
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, Constants.COMPONENT_INDEX_SERVICE_URL);

		this.indexProviderLoadBalancer = IndexServiceUtil.getIndexProviderLoadBalancer(indexProviderProps);

		// -----------------------------------------------------------------------------
		// Open service trackers
		// -----------------------------------------------------------------------------
		openNodeOSTracker(bundleContext);

		// --------------------------------------------------------------------------------
		// Start CLI commands
		// --------------------------------------------------------------------------------
		startNodeOSCommand(bundleContext);
		this.nodeOSCommand.startnodeos();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// --------------------------------------------------------------------------------
		// Stop CLI commands
		// --------------------------------------------------------------------------------
		this.nodeOSCommand.stopnodeos();
		stopNodeOSCommand(bundleContext);

		// -----------------------------------------------------------------------------
		// Close service trackers
		// -----------------------------------------------------------------------------
		closeNodeOSTracker();

		Activator.bundleContext = null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	protected void openNodeOSTracker(final BundleContext bundleContext) {
		nodeOSTracker = new ServiceTracker<NodeOS, NodeOS>(bundleContext, NodeOS.class, new ServiceTrackerCustomizer<NodeOS, NodeOS>() {
			@Override
			public NodeOS addingService(ServiceReference<NodeOS> reference) {
				NodeOS nodeOS = bundleContext.getService(reference);
				System.out.println(nodeOS.getLabel() + " is added.");

				startNodeOSWebService(nodeOS);
				return nodeOS;
			}

			@Override
			public void modifiedService(ServiceReference<NodeOS> reference, NodeOS nodeOS) {
				System.out.println(nodeOS.getLabel() + " is modified.");

				stopNodeOSWebService();
				startNodeOSWebService(nodeOS);
			}

			@Override
			public void removedService(ServiceReference<NodeOS> reference, NodeOS nodeOS) {
				System.out.println(nodeOS.getLabel() + " is removed.");

				stopNodeOSWebService();
			}
		});
		nodeOSTracker.open();
	}

	protected void closeNodeOSTracker() {
		if (nodeOSTracker != null) {
			nodeOSTracker.close();
			nodeOSTracker = null;
		}
	}

	protected void startNodeOSWebService(NodeOS nodeOS) {
		this.nodeOSWebApp = new NodeOSWSApplication();
		this.nodeOSWebApp.setBundleContext(bundleContext);
		this.nodeOSWebApp.setContextRoot(nodeOS.getContextRoot());
		this.nodeOSWebApp.setIndexProvider(this.indexProviderLoadBalancer.createLoadBalancableIndexProvider());
		this.nodeOSWebApp.start();
	}

	protected void stopNodeOSWebService() {
		if (this.nodeOSWebApp != null) {
			this.nodeOSWebApp.stop();
			this.nodeOSWebApp = null;
		}
	}

	protected void startNodeOSCommand(BundleContext bundleContext) {
		this.nodeOSCommand = new NodeOSCommand();
		this.nodeOSCommand.activate(bundleContext);
	}

	protected void stopNodeOSCommand(BundleContext bundleContext) {
		if (this.nodeOSCommand != null) {
			this.nodeOSCommand.deactivate(bundleContext);
			this.nodeOSCommand = null;
		}
	}

}
