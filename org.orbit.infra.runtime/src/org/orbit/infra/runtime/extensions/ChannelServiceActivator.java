package org.orbit.infra.runtime.extensions;

import java.util.Map;

import org.orbit.infra.runtime.channel.service.ChannelService;
import org.orbit.infra.runtime.channel.service.ChannelServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivator;
import org.osgi.framework.BundleContext;

public class ChannelServiceActivator implements ServiceActivator {

	public static final String ID = "orbit.channel_service.service_activator";

	public static ChannelServiceActivator INSTANCE = new ChannelServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<String, Object> properties = context.getProperties();

		// Start ChannelService
		ChannelServiceImpl channelService = new ChannelServiceImpl(properties);
		channelService.start(bundleContext);

		process.adapt(ChannelService.class, channelService);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop ChannelService
		ChannelService channelService = process.getAdapter(ChannelService.class);
		if (channelService instanceof ChannelServiceImpl) {
			((ChannelServiceImpl) channelService).stop(bundleContext);
		}
	}

}
