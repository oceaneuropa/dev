package org.orbit.spirit.connector.gaia;

import java.util.Map;

import org.orbit.spirit.api.gaia.GAIAClient;
import org.origin.common.rest.client.ServiceConnector;

public class GAIAConnector extends ServiceConnector<GAIAClient> {

	public GAIAConnector() {
		super(GAIAClient.class);
	}

	@Override
	protected GAIAClient create(Map<String, Object> properties) {
		return new GAIAClientImpl(this, properties);
	}

}
