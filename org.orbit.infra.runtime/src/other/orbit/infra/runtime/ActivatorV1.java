package other.orbit.infra.runtime;

import java.util.Hashtable;
import java.util.Map;

// import org.orbit.datatube.runtime.datatube.service.DataTubeService;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.cli.InfraRuntimeCommand;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.orbit.infra.runtime.indexes.ws.IndexServiceAdapter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import other.orbit.infra.api.indexes.IndexProviderConnectorAdapterV1;
//import other.orbit.infra.api.indexes.IndexProviderConnectorV1;
//import other.orbit.infra.api.indexes.IndexProviderLoadBalancer;
//import other.orbit.infra.api.indexes.IndexServiceUtil;
import other.orbit.infra.runtime.channel.ws.ChannelServiceAdapterV1;

public class ActivatorV1 implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(ActivatorV1.class);

	protected static BundleContext bundleContext;
	protected static ActivatorV1 instance;

	public static BundleContext getContext() {
		return bundleContext;
	}

	public static ActivatorV1 getInstance() {
		return instance;
	}

	// protected IndexProviderConnectorAdapterV1 indexProviderConnectorAdapter;

	protected boolean hasIndexService;
	protected boolean autoStartIndexService;
	protected IndexServiceAdapter indexServiceAdapter;

	protected boolean hasChannelService;
	protected boolean autoStartChannelService;
	protected ChannelServiceAdapterV1 channelServiceAdapter;

	protected InfraRuntimeCommand servicesCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		ActivatorV1.bundleContext = bundleContext;
		ActivatorV1.instance = this;

		// Get the available components
		Map<Object, Object> configProps = new Hashtable<Object, Object>();

		PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.COMPONENT_INDEX_SERVICE_AUTOSTART);
		PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.COMPONENT_INDEX_SERVICE_NAME);

		// PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.DATATUBE__AUTOSTART);
		// PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.DATATUBE__NAME);

		autoStartIndexService = configProps.containsKey(InfraConstants.COMPONENT_INDEX_SERVICE_AUTOSTART) ? true : false;
		hasIndexService = configProps.containsKey(InfraConstants.COMPONENT_INDEX_SERVICE_NAME) ? true : false;

		// autoStartChannelService = configProps.containsKey(InfraConstants.DATATUBE__AUTOSTART) ? true : false;
		// hasChannelService = configProps.containsKey(InfraConstants.DATATUBE__NAME) ? true : false;

		LOG.info("autoStartIndexService = " + autoStartIndexService);
		LOG.info("hasIndexService = " + hasIndexService);

		LOG.info("autoStartChannelService = " + autoStartChannelService);
		LOG.info("hasChannelService = " + hasChannelService);

		// Start service adapters
		if (hasIndexService) {
			indexServiceAdapter = new IndexServiceAdapter(null);
			indexServiceAdapter.start(bundleContext);
		}

		// Start commands
		this.servicesCommand = new InfraRuntimeCommand();
		this.servicesCommand.start(bundleContext);

		if (autoStartIndexService) {
			this.servicesCommand.startservice(InfraRuntimeCommand.INDEX_SERVICE);
		}
		if (autoStartChannelService) {
			this.servicesCommand.startservice(InfraRuntimeCommand.CHANNEL);
		}

//		this.indexProviderConnectorAdapter = new IndexProviderConnectorAdapterV1() {
//			@Override
//			public void connectorAdded(IndexProviderConnectorV1 connector) {
//				doStart(ActivatorV1.bundleContext, connector);
//			}
//
//			@Override
//			public void connectorRemoved(IndexProviderConnectorV1 connector) {
//				doStop(ActivatorV1.bundleContext);
//			}
//		};
//		this.indexProviderConnectorAdapter.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		ActivatorV1.instance = null;
		ActivatorV1.bundleContext = null;

//		if (this.indexProviderConnectorAdapter != null) {
//			this.indexProviderConnectorAdapter.stop(bundleContext);
//			this.indexProviderConnectorAdapter = null;
//		}

		// Stop commands
		if (getIndexService() != null) {
			this.servicesCommand.stopservice(InfraRuntimeCommand.INDEX_SERVICE);
		}
		// if (getChannelService() != null) {
		// this.servicesCommand.stopservice(InfraRuntimeCommand.CHANNEL);
		// }

		// Stop service adapters
		if (indexServiceAdapter != null) {
			indexServiceAdapter.stop(bundleContext);
			indexServiceAdapter = null;
		}
	}

	public IndexService getIndexService() {
		return (indexServiceAdapter != null) ? indexServiceAdapter.getService() : null;
	}

	// public DataTubeService getChannelService() {
	// return (channelServiceAdapter != null) ? channelServiceAdapter.getService() : null;
	// }

//	protected void doStart(BundleContext bundleContext, IndexProviderConnectorV1 connector) {
//		// Get IndexProvider load balancer
//		// load properties from accessing index service
//		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
//		// PropertyUtil.loadProperty(bundleContext, indexProviderProps, org.orbit.infra.api.InfraConstants.ORBIT_INDEX_SERVICE_URL);
//		IndexProviderLoadBalancer indexProviderLoadBalancer = IndexServiceUtil.getIndexProviderLoadBalancer(connector, indexProviderProps);
//
//		if (hasChannelService) {
//			channelServiceAdapter = new ChannelServiceAdapterV1(indexProviderLoadBalancer);
//			channelServiceAdapter.start(bundleContext);
//		}
//	}

	protected void doStop(BundleContext bundleContext) {
		if (channelServiceAdapter != null) {
			channelServiceAdapter.stop(bundleContext);
			channelServiceAdapter = null;
		}
	}

}
