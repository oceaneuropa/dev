package org.orbit.spirit.connector.gaia;

import java.util.Map;

import org.orbit.platform.sdk.connector.IConnectorActivator;
import org.orbit.spirit.api.gaia.GAIAClient;
import org.origin.common.rest.client.ServiceConnector;

public class GAIAConnector extends ServiceConnector<GAIAClient> implements IConnectorActivator {

	public static final String ID = "org.orbit.spirit.connector.GAIAConnector";

	public GAIAConnector() {
		super(GAIAClient.class);
	}

	@Override
	protected GAIAClient create(Map<String, Object> properties) {
		return new GAIAClientImpl(this, properties);
	}

}
