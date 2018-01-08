package org.orbit.infra.connector.other;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexServiceLoadBalancer;
import org.orbit.infra.api.indexes.IndexServiceUtil;
import org.orbit.infra.api.indexes.other.IndexServiceConnectorAdapterV1;
import org.orbit.infra.api.indexes.other.IndexServiceConnectorV1;
import org.orbit.infra.connector.InfraConstants;
import org.orbit.infra.connector.channel.ChannelsConnectorImpl;
import org.orbit.infra.connector.indexes.IndexProviderConnectorImpl;
import org.orbit.infra.connector.indexes.IndexServiceConnectorImpl;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ActivatorV1 implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected IndexServiceConnectorAdapterV1 indexServiceConnectorAdapter;

	protected IndexServiceConnectorImpl indexServiceConnectorImpl;
	protected IndexProviderConnectorImpl indexProviderConnectorImpl;
	protected ChannelsConnectorImpl channelConnector;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		ActivatorV1.context = bundleContext;

		this.indexProviderConnectorImpl = new IndexProviderConnectorImpl();
		this.indexProviderConnectorImpl.start(bundleContext);

		this.indexServiceConnectorImpl = new IndexServiceConnectorImpl();
		this.indexServiceConnectorImpl.start(bundleContext);

		this.channelConnector = new ChannelsConnectorImpl();
		this.channelConnector.start(bundleContext);

		this.indexServiceConnectorAdapter = new IndexServiceConnectorAdapterV1() {
			@Override
			public void connectorAdded(IndexServiceConnectorV1 connector) {
				doStart(ActivatorV1.context, connector);
			}

			@Override
			public void connectorRemoved(IndexServiceConnectorV1 connector) {
				doStop(ActivatorV1.context);
			}
		};
		this.indexServiceConnectorAdapter.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.channelConnector != null) {
			this.channelConnector.stop(bundleContext);
			this.channelConnector = null;
		}

		if (this.indexServiceConnectorImpl != null) {
			this.indexServiceConnectorImpl.stop(bundleContext);
			this.indexServiceConnectorImpl = null;
		}

		if (this.indexProviderConnectorImpl != null) {
			this.indexProviderConnectorImpl.stop(bundleContext);
			this.indexProviderConnectorImpl = null;
		}

		if (this.indexServiceConnectorAdapter != null) {
			this.indexServiceConnectorAdapter.stop(bundleContext);
			this.indexServiceConnectorAdapter = null;
		}

		ActivatorV1.context = null;
	}

	protected void doStart(BundleContext bundleContext, IndexServiceConnectorV1 connector) {
		// Get load balancer for IndexProvider
		// Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		// PropertyUtil.loadProperty(bundleContext, indexProviderProps, InfraConstants.ORBIT_INDEX_SERVICE_URL);
		// IndexServiceLoadBalancer indexServiceLoadBalancer = IndexServiceUtil.getIndexServiceLoadBalancer(connector, indexProviderProps);

		// this.servicesCommand = new ServicesCommand(indexServiceLoadBalancer.createLoadBalancableIndexService());
		// this.servicesCommand = new ServicesCommand();
		// this.servicesCommand.start(bundleContext);
		//
		// this.indexServiceCommand = new IndexServiceCommand();
		// this.indexServiceCommand.start(bundleContext);
		//
		// this.channelCommand = new ChannelCommand();
		// this.channelCommand.start(bundleContext);
	}

	protected void doStop(BundleContext bundleContext) {
		// if (this.servicesCommand != null) {
		// this.servicesCommand.stop(bundleContext);
		// this.servicesCommand = null;
		// }
		//
		// if (this.indexServiceCommand != null) {
		// this.indexServiceCommand.stop(bundleContext);
		// this.indexServiceCommand = null;
		// }
		//
		// if (this.channelCommand != null) {
		// this.channelCommand.stop(bundleContext);
		// this.channelCommand = null;
		// }
	}

}
