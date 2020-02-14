package org.orbit.infra.runtime.extensions.extensionregistry;

import java.util.Map;

import org.orbit.infra.runtime.lb.ExtensionRegistryWSApplicationRelay;
import org.orbit.infra.runtime.lb.InfraRelays;
import org.orbit.platform.sdk.ProcessContext;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.IProcess;
import org.osgi.framework.BundleContext;

public class ExtensionRegistryRelayActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.ExtensionRegistryRelayActivator";

	public static ExtensionRegistryRelayActivator INSTANCE = new ExtensionRegistryRelayActivator();

	@Override
	public void start(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> initProperties = context.getProperties();

		// Start ExtensionRegistry relay
		ExtensionRegistryWSApplicationRelay relay = InfraRelays.getInstance().createExtensionRegistryRelay(bundleContext, initProperties);
		if (relay != null) {
			relay.start(bundleContext);
			process.adapt(ExtensionRegistryWSApplicationRelay.class, relay);
		}
	}

	@Override
	public void stop(ProcessContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop ExtensionRegistry relay
		ExtensionRegistryWSApplicationRelay relay = process.getAdapter(ExtensionRegistryWSApplicationRelay.class);
		if (relay != null) {
			relay.stop(bundleContext);
		}
	}

}
