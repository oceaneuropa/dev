package org.orbit.component.connector.tier4.mission;

import java.util.Map;

import org.orbit.component.api.tier4.mission.MissionControlClient;
import org.origin.common.rest.client.ServiceConnector;

public class MissionControlConnector extends ServiceConnector<MissionControlClient> {

	public MissionControlConnector() {
		super(MissionControlClient.class);
	}

	@Override
	protected MissionControlClient create(Map<String, Object> properties) {
		return new MissionControlClientImpl(this, properties);
	}

}
