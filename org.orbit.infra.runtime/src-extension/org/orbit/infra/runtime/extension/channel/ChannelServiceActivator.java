package org.orbit.infra.runtime.extension.channel;

import java.util.Map;

import org.orbit.infra.runtime.channel.service.ChannelService;
import org.orbit.infra.runtime.channel.service.ChannelServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class ChannelServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.ChannelServiceActivator";

	public static ChannelServiceActivator INSTANCE = new ChannelServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

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
		if (channelService instanceof LifecycleAware) {
			((LifecycleAware) channelService).stop(bundleContext);
		}
	}

}
