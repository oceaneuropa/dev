package org.orbit.component.connector.tier4.mission;

import java.util.Map;

import org.orbit.component.api.tier4.mission.MissionControl;
import org.orbit.component.connector.OrbitConstants;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;

public class MissionControlImpl extends ServiceClientImpl<MissionControl, MissionControlWSClient> implements MissionControl {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public MissionControlImpl(ServiceConnector<MissionControl> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected MissionControlWSClient createWSClient(Map<String, Object> properties) {
		String realm = (String) this.properties.get(OrbitConstants.REALM);
		String username = (String) this.properties.get(OrbitConstants.USERNAME);
		String fullUrl = (String) this.properties.get(OrbitConstants.URL);

		ClientConfiguration config = ClientConfiguration.create(realm, username, fullUrl);
		return new MissionControlWSClient(config);
	}

	@Override
	public String getURL() {
		String fullUrl = (String) properties.get(OrbitConstants.URL);
		return fullUrl;
	}

}
