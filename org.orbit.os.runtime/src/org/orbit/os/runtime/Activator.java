package org.orbit.os.runtime;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexProviderConnector;
import org.orbit.infra.api.indexes.IndexProviderConnectorAdapter;
import org.orbit.infra.api.indexes.IndexProviderLoadBalancer;
import org.orbit.infra.api.indexes.IndexServiceUtil;
import org.orbit.os.runtime.cli.OSCommand;
import org.orbit.os.runtime.service.GAIA;
import org.orbit.os.runtime.util.SetupUtil;
import org.orbit.os.runtime.ws.GaiaAdapter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static BundleContext bundleContext;
	protected static Activator instance;

	static BundleContext getContext() {
		return bundleContext;
	}

	public static Activator getInstance() {
		return instance;
	}

	protected IndexProviderConnectorAdapter indexProviderConnectorAdapter;
	protected OSCommand OSCommand;
	protected GaiaAdapter gaiaAdapter;

	public GAIA getGAIA() {
		return (this.gaiaAdapter != null) ? this.gaiaAdapter.getService() : null;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");

		Activator.bundleContext = bundleContext;
		Activator.instance = this;

		// Start commands
		this.OSCommand = new OSCommand();
		this.OSCommand.start(bundleContext);
		this.OSCommand.startGAIA();

		this.indexProviderConnectorAdapter = new IndexProviderConnectorAdapter() {
			@Override
			public void connectorAdded(IndexProviderConnector connector) {
				doStart(Activator.bundleContext, connector);
			}

			@Override
			public void connectorRemoved(IndexProviderConnector connector) {
				doStop(Activator.bundleContext);
			}
		};
		this.indexProviderConnectorAdapter.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		if (this.indexProviderConnectorAdapter != null) {
			this.indexProviderConnectorAdapter.stop(bundleContext);
			this.indexProviderConnectorAdapter = null;
		}

		// Stop commands
		if (this.OSCommand != null) {
			this.OSCommand.stopGAIA();
			this.OSCommand.stop(bundleContext);
			this.OSCommand = null;
		}

		Activator.instance = null;
		Activator.bundleContext = null;
	}

	/**
	 * 
	 * @param bundleContext
	 * @param connector
	 */
	protected void doStart(BundleContext bundleContext, IndexProviderConnector connector) {
		// Get IndexProvider load balancer
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		SetupUtil.loadNodeConfigIniProperties(bundleContext, indexProviderProps);
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, OSConstants.COMPONENT_INDEX_SERVICE_URL);
		IndexProviderLoadBalancer indexProviderLoadBalancer = IndexServiceUtil.getIndexProviderLoadBalancer(connector, indexProviderProps);

		// Start service adapter
		gaiaAdapter = new GaiaAdapter(indexProviderLoadBalancer);
		gaiaAdapter.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	protected void doStop(BundleContext bundleContext) {
		// Stop service adapter
		if (gaiaAdapter != null) {
			gaiaAdapter.stop(bundleContext);
			gaiaAdapter = null;
		}
	}

}
