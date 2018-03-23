package org.orbit.infra.runtime.extension;

import java.util.Map;

import org.orbit.infra.runtime.relay.IndexServiceWSApplicationRelay;
import org.orbit.infra.runtime.relay.InfraRelays;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivator;
import org.osgi.framework.BundleContext;

public class IndexServiceRelayActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.IndexServiceRelayActivator";

	public static IndexServiceRelayActivator INSTANCE = new IndexServiceRelayActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start IndexService relay
		IndexServiceWSApplicationRelay relay = InfraRelays.getInstance().createIndexServiceRelay(bundleContext, properties);
		if (relay != null) {
			relay.start(bundleContext);
			process.adapt(IndexServiceWSApplicationRelay.class, relay);
		}
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop IndexService relay
		IndexServiceWSApplicationRelay relay = process.getAdapter(IndexServiceWSApplicationRelay.class);
		if (relay != null) {
			relay.stop(bundleContext);
		}
	}

}
