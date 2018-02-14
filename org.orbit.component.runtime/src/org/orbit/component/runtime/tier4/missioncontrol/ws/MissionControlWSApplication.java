package org.orbit.component.runtime.tier4.missioncontrol.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlService;

public class MissionControlWSApplication extends OrbitWSApplication {

	public MissionControlWSApplication(final MissionControlService service, int feature) {
		super(service.getContextRoot(), feature);
		adapt(MissionControlService.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(MissionControlService.class);
			}
		});
		register(MissionControlWSResource.class);
	}

}
