package org.orbit.component.runtime.extensions;

import org.orbit.component.runtime.tier4.mission.service.MissionControlServiceImpl;
import org.orbit.os.runtime.api.ServiceControl;
import org.osgi.framework.BundleContext;

public class MissionControlServiceControl implements ServiceControl {

	public static MissionControlServiceControl INSTANCE = new MissionControlServiceControl();

	protected MissionControlServiceImpl missionControlService;

	@Override
	public void start(BundleContext bundleContext) {
		MissionControlServiceImpl missionControlService = new MissionControlServiceImpl();
		missionControlService.start(bundleContext);
		this.missionControlService = missionControlService;
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.missionControlService != null) {
			this.missionControlService.stop(bundleContext);
			this.missionControlService = null;
		}
	}

}
