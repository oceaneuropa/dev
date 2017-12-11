package org.orbit.infra.runtime;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexProviderConnector;
import org.orbit.infra.api.indexes.IndexProviderConnectorAdapter;
import org.orbit.infra.api.indexes.IndexProviderLoadBalancer;
import org.orbit.infra.api.indexes.IndexServiceUtil;
import org.orbit.infra.runtime.channel.service.ChannelService;
import org.orbit.infra.runtime.channel.ws.ChannelServiceAdapter;
import org.orbit.infra.runtime.cli.ServicesCommand;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.orbit.infra.runtime.indexes.ws.IndexServiceAdapter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static BundleContext bundleContext;
	protected static Activator instance;

	public static BundleContext getContext() {
		return bundleContext;
	}

	public static Activator getInstance() {
		return instance;
	}

	protected IndexProviderConnectorAdapter indexProviderConnectorAdapter;

	protected boolean hasIndexService;
	protected boolean autoStartIndexService;
	protected IndexServiceAdapter indexServiceAdapter;

	protected boolean hasChannelService;
	protected boolean autoStartChannelService;
	protected ChannelServiceAdapter channelServiceAdapter;

	protected ServicesCommand servicesCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.bundleContext = bundleContext;
		Activator.instance = this;

		// Get the available components
		Map<Object, Object> configProps = new Hashtable<Object, Object>();

		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_INDEX_SERVICE_AUTOSTART);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_INDEX_SERVICE_NAME);

		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_CHANNEL_AUTOSTART);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_CHANNEL_NAME);

		autoStartIndexService = configProps.containsKey(OrbitConstants.COMPONENT_INDEX_SERVICE_AUTOSTART) ? true : false;
		hasIndexService = configProps.containsKey(OrbitConstants.COMPONENT_INDEX_SERVICE_NAME) ? true : false;

		autoStartChannelService = configProps.containsKey(OrbitConstants.COMPONENT_CHANNEL_AUTOSTART) ? true : false;
		hasChannelService = configProps.containsKey(OrbitConstants.COMPONENT_CHANNEL_NAME) ? true : false;

		LOG.info("autoStartIndexService = " + autoStartIndexService);
		LOG.info("hasIndexService = " + hasIndexService);

		LOG.info("autoStartChannelService = " + autoStartChannelService);
		LOG.info("hasChannelService = " + hasChannelService);

		// Start service adapters
		if (hasIndexService) {
			indexServiceAdapter = new IndexServiceAdapter();
			indexServiceAdapter.start(bundleContext);
		}

		// Start commands
		this.servicesCommand = new ServicesCommand();
		this.servicesCommand.start(bundleContext);

		if (autoStartIndexService) {
			this.servicesCommand.startservice(ServicesCommand.INDEX_SERVICE);
		}
		if (autoStartChannelService) {
			this.servicesCommand.startservice(ServicesCommand.CHANNEL);
		}

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
		Activator.instance = null;
		Activator.bundleContext = null;

		if (this.indexProviderConnectorAdapter != null) {
			this.indexProviderConnectorAdapter.stop(bundleContext);
			this.indexProviderConnectorAdapter = null;
		}

		// Stop commands
		if (getIndexService() != null) {
			this.servicesCommand.stopservice(ServicesCommand.INDEX_SERVICE);
		}
		if (getChannelService() != null) {
			this.servicesCommand.stopservice(ServicesCommand.CHANNEL);
		}

		// Stop service adapters
		if (indexServiceAdapter != null) {
			indexServiceAdapter.stop(bundleContext);
			indexServiceAdapter = null;
		}
	}

	public IndexService getIndexService() {
		return (indexServiceAdapter != null) ? indexServiceAdapter.getService() : null;
	}

	public ChannelService getChannelService() {
		return (channelServiceAdapter != null) ? channelServiceAdapter.getService() : null;
	}

	protected void doStart(BundleContext bundleContext, IndexProviderConnector connector) {
		// Get IndexProvider load balancer
		// load properties from accessing index service
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, OrbitConstants.COMPONENT_INDEX_SERVICE_URL);
		IndexProviderLoadBalancer indexProviderLoadBalancer = IndexServiceUtil.getIndexProviderLoadBalancer(connector, indexProviderProps);

		if (hasChannelService) {
			channelServiceAdapter = new ChannelServiceAdapter(indexProviderLoadBalancer);
			channelServiceAdapter.start(bundleContext);
		}
	}

	protected void doStop(BundleContext bundleContext) {
		if (channelServiceAdapter != null) {
			channelServiceAdapter.stop(bundleContext);
			channelServiceAdapter = null;
		}
	}

}
