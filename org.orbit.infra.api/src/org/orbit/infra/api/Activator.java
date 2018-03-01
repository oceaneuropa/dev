package org.orbit.infra.api;

import org.orbit.infra.api.cli.ChannelCommand;
import org.orbit.infra.api.cli.IndexServiceCommand;
import org.orbit.infra.api.cli.ServicesCommand;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static Activator plugin;

	public static Activator getDefault() {
		return plugin;
	}

	public static BundleContext getContext() {
		return context;
	}

	protected ServicesCommand servicesCommand;
	protected IndexServiceCommand indexServiceCommand;
	protected ChannelCommand channelCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.plugin = this;
		Activator.context = bundleContext;

		InfraClients.getInstance().start(bundleContext);

		// Start commands
		this.servicesCommand = new ServicesCommand();
		this.servicesCommand.start(bundleContext);

		this.indexServiceCommand = new IndexServiceCommand();
		this.indexServiceCommand.start(bundleContext);

		this.channelCommand = new ChannelCommand();
		this.channelCommand.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// Stop commands
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

		InfraClients.getInstance().stop(bundleContext);

		Activator.context = null;
		Activator.plugin = null;
	}

}

// protected IndexServiceConnectorAdapter indexServiceConnectorAdapter;
// protected IndexProviderConnectorAdapter indexProviderConnectorAdapter;
// protected ChannelsConnectorAdapter channelsConnectorAdapter;

// this.indexServiceConnectorAdapter = new IndexServiceConnectorAdapter();
// this.indexServiceConnectorAdapter.start(bundleContext);
//
// this.indexProviderConnectorAdapter = new IndexProviderConnectorAdapter();
// this.indexProviderConnectorAdapter.start(bundleContext);
//
// this.channelsConnectorAdapter = new ChannelsConnectorAdapter();
// this.channelsConnectorAdapter.start(bundleContext);

// if (this.indexProviderConnectorAdapter != null) {
// this.indexProviderConnectorAdapter.stop(bundleContext);
// this.indexProviderConnectorAdapter = null;
// }
//
// if (this.indexServiceConnectorAdapter != null) {
// this.indexServiceConnectorAdapter.stop(bundleContext);
// this.indexServiceConnectorAdapter = null;
// }
//
// if (this.channelsConnectorAdapter != null) {
// this.channelsConnectorAdapter.stop(bundleContext);
// this.channelsConnectorAdapter = null;
// }

// public IndexServiceConnector getIndexServiceConnector() {
// return this.indexServiceConnectorAdapter != null ? this.indexServiceConnectorAdapter.getConnector() : null;
// }
//
// public IndexProviderConnector getIndexProviderConnector() {
// return this.indexProviderConnectorAdapter != null ? this.indexProviderConnectorAdapter.getConnector() : null;
// }
//
// public ChannelsConnector getChannelConnector() {
// return this.channelsConnectorAdapter != null ? this.channelsConnectorAdapter.getConnector() : null;
// }
