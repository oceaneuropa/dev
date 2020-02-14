package org.orbit.infra.runtime.extensions.configregistry;

import java.util.Map;

import org.orbit.infra.runtime.lb.ConfigRegistryWSApplicationRelay;
import org.orbit.infra.runtime.lb.InfraRelays;
import org.orbit.platform.sdk.ProcessContext;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.IProcess;
import org.osgi.framework.BundleContext;

public class ConfigRegistryRelayActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.ConfigRegistryRelayActivator";

	public static ConfigRegistryRelayActivator INSTANCE = new ConfigRegistryRelayActivator();

	@Override
	public void start(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> initProperties = context.getProperties();

		// Start relay
		ConfigRegistryWSApplicationRelay relay = InfraRelays.getInstance().createConfigRegistryRelay(bundleContext, initProperties);
		if (relay != null) {
			relay.start(bundleContext);
			process.adapt(ConfigRegistryWSApplicationRelay.class, relay);
		}
	}

	@Override
	public void stop(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop relay
		ConfigRegistryWSApplicationRelay relay = process.getAdapter(ConfigRegistryWSApplicationRelay.class);
		if (relay != null) {
			relay.stop(bundleContext);
		}
	}

}
