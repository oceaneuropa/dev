package org.orbit.infra.runtime.extensions;

import java.util.Map;

import org.orbit.infra.runtime.relay.ChannelWSApplicationRelay;
import org.orbit.infra.runtime.relay.InfraRelays;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivator;
import org.osgi.framework.BundleContext;

public class ChannelServiceRelayActivator implements ServiceActivator {

	public static final String ID = "orbit.channel_service.relay.service_activator";

	public static ChannelServiceRelayActivator INSTANCE = new ChannelServiceRelayActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> initProperties = context.getProperties();

		// Start ChannelService relay
		ChannelWSApplicationRelay relay = InfraRelays.getInstance().createChannelRelay(bundleContext, initProperties);
		relay.start(bundleContext);

		process.adapt(ChannelWSApplicationRelay.class, relay);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop ChannelService relay
		ChannelWSApplicationRelay relay = process.getAdapter(ChannelWSApplicationRelay.class);
		if (relay != null) {
			relay.stop(bundleContext);
		}
	}

}
