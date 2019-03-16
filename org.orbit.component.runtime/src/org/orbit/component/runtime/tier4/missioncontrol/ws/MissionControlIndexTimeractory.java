package org.orbit.component.runtime.tier4.missioncontrol.ws;

import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlService;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;

public class MissionControlIndexTimeractory implements ServiceIndexTimerFactory<MissionControlService> {

	@Override
	public MissionControlIndexTimer create(MissionControlService service) {
		return new MissionControlIndexTimer(service);
	}

}
