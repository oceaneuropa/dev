package org.orbit.spirit.connector.gaia;

import java.util.Map;

import org.orbit.spirit.api.gaia.GaiaClient;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;

public class GaiaClientImpl extends ServiceClientImpl<GaiaClient, GaiaWSClient> implements GaiaClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public GaiaClientImpl(ServiceConnector<GaiaClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected GaiaWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new GaiaWSClient(config);
	}

}
