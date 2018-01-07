package org.orbit.infra.connector;

import org.orbit.infra.connector.channel.ChannelsConnectorImpl;
import org.orbit.infra.connector.indexes.IndexProviderConnectorImpl;
import org.orbit.infra.connector.indexes.IndexServiceConnectorImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected IndexServiceConnectorImpl indexServiceConnectorImpl;
	protected IndexProviderConnectorImpl indexProviderConnectorImpl;
	protected ChannelsConnectorImpl channelConnector;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// Start connectors
		this.indexProviderConnectorImpl = new IndexProviderConnectorImpl();
		this.indexProviderConnectorImpl.start(bundleContext);

		this.indexServiceConnectorImpl = new IndexServiceConnectorImpl();
		this.indexServiceConnectorImpl.start(bundleContext);

		this.channelConnector = new ChannelsConnectorImpl();
		this.channelConnector.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// Stop connectors
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

		Activator.context = null;
	}

}
