package org.orbit.infra.runtime.extensions.channelservice;

import java.util.Map;

import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.orbit.infra.runtime.datatube.service.impl.DataTubeServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class ChannelServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.ChannelServiceActivator";

	public static ChannelServiceActivator INSTANCE = new ChannelServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start ChannelService
		DataTubeServiceImpl channelService = new DataTubeServiceImpl(properties);
		channelService.start(bundleContext);

		process.adapt(DataTubeService.class, channelService);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop ChannelService
		DataTubeService channelService = process.getAdapter(DataTubeService.class);
		if (channelService instanceof LifecycleAware) {
			((LifecycleAware) channelService).stop(bundleContext);
		}
	}

}
