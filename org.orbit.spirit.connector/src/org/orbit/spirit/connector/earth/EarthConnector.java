package org.orbit.spirit.connector.earth;

import java.util.Map;

import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.orbit.spirit.api.earth.EarthClient;
import org.origin.common.rest.client.ServiceConnector;

public class EarthConnector extends ServiceConnector<EarthClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.spirit.connector.EarthConnector";

	public EarthConnector() {
		super(EarthClient.class);
	}

	@Override
	protected EarthClient create(Map<String, Object> properties) {
		return new EarthClientImpl(this, properties);
	}

}
