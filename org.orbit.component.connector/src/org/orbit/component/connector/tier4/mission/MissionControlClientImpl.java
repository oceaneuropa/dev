package org.orbit.component.connector.tier4.mission;

import java.util.Map;

import org.orbit.component.api.tier4.missioncontrol.MissionControlClient;
import org.orbit.component.connector.OrbitConstants;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;

public class MissionControlClientImpl extends ServiceClientImpl<MissionControlClient, MissionControlWSClient> implements MissionControlClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public MissionControlClientImpl(ServiceConnector<MissionControlClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected MissionControlWSClient createWSClient(Map<String, Object> properties) {
		String realm = (String) properties.get(OrbitConstants.REALM);
		String username = (String) properties.get(OrbitConstants.USERNAME);
		String fullUrl = (String) properties.get(OrbitConstants.URL);

		ClientConfiguration config = ClientConfiguration.create(realm, username, fullUrl);
		return new MissionControlWSClient(config);
	}

	@Override
	public String getURL() {
		String fullUrl = (String) this.properties.get(OrbitConstants.URL);
		return fullUrl;
	}

}
