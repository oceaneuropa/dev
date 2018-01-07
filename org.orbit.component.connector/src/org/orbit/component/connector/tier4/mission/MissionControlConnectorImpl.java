package org.orbit.component.connector.tier4.mission;

import java.util.Map;

import org.orbit.component.api.tier4.mission.MissionControl;
import org.origin.common.rest.client.ServiceConnector;

public class MissionControlConnectorImpl extends ServiceConnector<MissionControl> {

	public MissionControlConnectorImpl() {
		super(MissionControl.class);
	}

	@Override
	protected MissionControl create(Map<String, Object> properties) {
		return new MissionControlImpl(this, properties);
	}

}
