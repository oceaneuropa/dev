package org.orbit.component.connector.tier4.missioncontrol;

import java.util.Map;

import org.orbit.component.api.tier4.missioncontrol.MissionControlClient;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.client.WSClientConstants;

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
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new MissionControlWSClient(config);
	}

	@Override
	public String getURL() {
		String url = (String) this.properties.get(WSClientConstants.URL);
		return url;
	}

}
