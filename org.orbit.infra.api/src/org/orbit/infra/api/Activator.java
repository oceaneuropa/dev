package org.orbit.infra.api;

import org.orbit.infra.api.util.ConfigRegistryConfigPropertiesHandler;
import org.orbit.infra.api.util.ExtensionsRegister;
import org.orbit.infra.api.util.InfraClients;
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

	protected ExtensionsRegister extensionsRegister;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.plugin = this;
		Activator.context = bundleContext;

		ConfigRegistryConfigPropertiesHandler.getInstance().start(bundleContext);

		InfraClients infraClients = InfraClients.getInstance();
		infraClients.start(bundleContext);

		this.extensionsRegister = new ExtensionsRegister();
		this.extensionsRegister.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.extensionsRegister != null) {
			this.extensionsRegister.stop(bundleContext);
		}

		InfraClients.getInstance().stop(bundleContext);

		ConfigRegistryConfigPropertiesHandler.getInstance().stop(bundleContext);

		Activator.context = null;
		Activator.plugin = null;
	}

}

// ClassLoader loader1 = this.getClass().getClassLoader();
// ClassLoader loader2 = infraClients.getClass().getClassLoader();

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
