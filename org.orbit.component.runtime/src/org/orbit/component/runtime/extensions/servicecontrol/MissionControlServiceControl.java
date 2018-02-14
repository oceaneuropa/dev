package org.orbit.component.runtime.extensions.servicecontrol;

import java.util.Map;

import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlServiceImpl;
import org.orbit.platform.sdk.servicecontrol.ServiceControlImpl;
import org.osgi.framework.BundleContext;

public class MissionControlServiceControl extends ServiceControlImpl {

	public static MissionControlServiceControl INSTANCE = new MissionControlServiceControl();

	protected MissionControlServiceImpl missionControlService;

	@Override
	public void start(BundleContext bundleContext, Map<String, Object> properties) {
		MissionControlServiceImpl missionControlService = new MissionControlServiceImpl();
		missionControlService.start(bundleContext);
		this.missionControlService = missionControlService;
	}

	@Override
	public void stop(BundleContext bundleContext, Map<String, Object> properties) {
		if (this.missionControlService != null) {
			this.missionControlService.stop(bundleContext);
			this.missionControlService = null;
		}
	}

}
