package org.orbit.component.runtime.extension.missioncontrol;

import java.util.Map;

import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlService;
import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlServiceImpl;
import org.orbit.platform.sdk.IProcessContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class MissionControlServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.MissionControlServiceActivator";

	public static MissionControlServiceActivator INSTANCE = new MissionControlServiceActivator();

	@Override
	public void start(IProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start MissionControlService
		MissionControlServiceImpl missionControlService = new MissionControlServiceImpl(properties);
		missionControlService.start(bundleContext);

		process.adapt(MissionControlService.class, missionControlService);
	}

	@Override
	public void stop(IProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop MissionControlService
		MissionControlService missionControlService = process.getAdapter(MissionControlService.class);
		if (missionControlService instanceof LifecycleAware) {
			((LifecycleAware) missionControlService).stop(bundleContext);
		}
	}

}
