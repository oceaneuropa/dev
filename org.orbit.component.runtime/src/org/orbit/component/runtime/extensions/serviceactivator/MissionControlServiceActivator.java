package org.orbit.component.runtime.extensions.serviceactivator;

import java.util.Map;

import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlService;
import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivatorImpl;
import org.osgi.framework.BundleContext;

public class MissionControlServiceActivator extends ServiceActivatorImpl {

	public static final String ID = "component.mission_control.service_activator";

	public static MissionControlServiceActivator INSTANCE = new MissionControlServiceActivator();

	@Override
	public String getProcessName() {
		return "MissionControl";
	}

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<String, Object> properties = context.getProperties();

		// Start MissionControlService
		MissionControlServiceImpl missionControlService = new MissionControlServiceImpl(properties);
		missionControlService.start(bundleContext);

		process.adapt(MissionControlService.class, missionControlService);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop MissionControlService
		MissionControlService missionControlService = process.getAdapter(MissionControlService.class);
		if (missionControlService instanceof MissionControlServiceImpl) {
			((MissionControlServiceImpl) missionControlService).stop(bundleContext);
		}
	}

}
