package org.orbit.infra.connector;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexServiceConnector;
import org.orbit.infra.api.indexes.IndexServiceConnectorAdapter;
import org.orbit.infra.api.indexes.IndexServiceLoadBalancer;
import org.orbit.infra.api.indexes.IndexServiceUtil;
import org.orbit.infra.connector.channel.ChannelsConnectorImpl;
import org.orbit.infra.connector.cli.ChannelCommand;
import org.orbit.infra.connector.cli.IndexServiceCommand;
import org.orbit.infra.connector.cli.ServicesCommand;
import org.orbit.infra.connector.indexes.IndexProviderConnectorImpl;
import org.orbit.infra.connector.indexes.IndexServiceConnectorImpl;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected IndexServiceConnectorAdapter indexServiceConnectorAdapter;

	protected IndexServiceConnectorImpl indexServiceConnectorImpl;
	protected IndexProviderConnectorImpl indexProviderConnectorImpl;
	protected ChannelsConnectorImpl channelConnector;

	protected ServicesCommand servicesCommand;
	protected IndexServiceCommand indexServiceCommand;
	protected ChannelCommand channelCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		this.indexServiceConnectorImpl = new IndexServiceConnectorImpl();
		this.indexServiceConnectorImpl.start(bundleContext);

		this.indexProviderConnectorImpl = new IndexProviderConnectorImpl();
		this.indexProviderConnectorImpl.start(bundleContext);

		this.channelConnector = new ChannelsConnectorImpl();
		this.channelConnector.start(bundleContext);

		this.indexServiceConnectorAdapter = new IndexServiceConnectorAdapter() {
			@Override
			public void connectorAdded(IndexServiceConnector connector) {
				doStart(Activator.context, connector);
			}

			@Override
			public void connectorRemoved(IndexServiceConnector connector) {
				doStop(Activator.context);
			}
		};
		this.indexServiceConnectorAdapter.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.indexProviderConnectorImpl != null) {
			this.indexProviderConnectorImpl.stop(bundleContext);
			this.indexProviderConnectorImpl = null;
		}

		if (this.indexServiceConnectorImpl != null) {
			this.indexServiceConnectorImpl.stop(bundleContext);
			this.indexServiceConnectorImpl = null;
		}

		if (this.channelConnector != null) {
			this.channelConnector.stop(bundleContext);
			this.channelConnector = null;
		}

		if (this.indexServiceConnectorAdapter != null) {
			this.indexServiceConnectorAdapter.stop(bundleContext);
			this.indexServiceConnectorAdapter = null;
		}

		Activator.context = null;
	}

	protected void doStart(BundleContext bundleContext, IndexServiceConnector connector) {
		// Get load balancer for IndexProvider
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, OrbitConstants.COMPONENT_INDEX_SERVICE_URL_PROP);
		IndexServiceLoadBalancer indexServiceLoadBalancer = IndexServiceUtil.getIndexServiceLoadBalancer(connector, indexProviderProps);

		this.servicesCommand = new ServicesCommand(indexServiceLoadBalancer.createLoadBalancableIndexService());
		this.servicesCommand.start(bundleContext);

		this.indexServiceCommand = new IndexServiceCommand();
		this.indexServiceCommand.start(bundleContext);

		this.channelCommand = new ChannelCommand();
		this.channelCommand.start(bundleContext);
	}

	protected void doStop(BundleContext bundleContext) {
		if (this.servicesCommand != null) {
			this.servicesCommand.stop(bundleContext);
			this.servicesCommand = null;
		}

		if (this.indexServiceCommand != null) {
			this.indexServiceCommand.stop(bundleContext);
			this.indexServiceCommand = null;
		}

		if (this.channelCommand != null) {
			this.channelCommand.stop(bundleContext);
			this.channelCommand = null;
		}
	}

}
