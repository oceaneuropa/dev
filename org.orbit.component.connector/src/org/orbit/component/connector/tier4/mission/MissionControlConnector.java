package org.orbit.component.connector.tier4.mission;

import java.util.Map;

import org.orbit.component.api.tier4.mission.MissionControl;
import org.origin.common.rest.client.ServiceConnector;

public class MissionControlConnector extends ServiceConnector<MissionControl> {

	public MissionControlConnector() {
		super(MissionControl.class);
	}

	@Override
	protected MissionControl create(Map<String, Object> properties) {
		return new MissionControlImpl(this, properties);
	}

}
