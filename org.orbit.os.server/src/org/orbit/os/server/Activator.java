package org.orbit.os.server;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.os.cli.AppCommand;
import org.orbit.os.server.service.NodeOS;
import org.orbit.os.server.util.SetupUtil;
import org.orbit.os.server.ws.NodeOSAdapter;
import org.origin.common.util.PropertyUtil;
import org.origin.mgm.client.api.IndexServiceUtil;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static BundleContext bundleContext;
	protected static NodeOSAdapter nodeOSAdapter;

	public static BundleContext getContext() {
		return bundleContext;
	}

	public static NodeOS getNodeOS() {
		return (nodeOSAdapter != null) ? nodeOSAdapter.getService() : null;
	}

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected AppCommand nodeOSCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");

		Activator.bundleContext = bundleContext;

		// Get IndexProvider load balancer
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		SetupUtil.loadNodeConfigIniProperties(bundleContext, indexProviderProps);
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, Constants.COMPONENT_INDEX_SERVICE_URL);
		this.indexProviderLoadBalancer = IndexServiceUtil.getIndexProviderLoadBalancer(indexProviderProps);

		// Start service adapter
		nodeOSAdapter = new NodeOSAdapter(this.indexProviderLoadBalancer);
		nodeOSAdapter.start(bundleContext);

		// Start commands
		this.nodeOSCommand = new AppCommand();
		this.nodeOSCommand.start(bundleContext);
		this.nodeOSCommand.startnodeos();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		// Stop commands
		if (this.nodeOSCommand != null) {
			this.nodeOSCommand.stopnodeos();
			this.nodeOSCommand.stop(bundleContext);
			this.nodeOSCommand = null;
		}

		// Stop service adapter
		if (nodeOSAdapter != null) {
			nodeOSAdapter.stop(bundleContext);
			nodeOSAdapter = null;
		}

		Activator.bundleContext = null;
	}

}
