package org.orbit.spirit.connector.gaia;

import java.util.Map;

import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.orbit.spirit.api.gaia.GaiaClient;
import org.origin.common.rest.client.ServiceConnector;

public class GaiaConnector extends ServiceConnector<GaiaClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.spirit.connector.GAIAConnector";

	public GaiaConnector() {
		super(GaiaClient.class);
	}

	@Override
	protected GaiaClient create(Map<String, Object> properties) {
		return new GaiaClientImpl(this, properties);
	}

}
