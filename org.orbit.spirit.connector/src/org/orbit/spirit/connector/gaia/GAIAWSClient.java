package org.orbit.spirit.connector.gaia;

import javax.ws.rs.client.Invocation.Builder;

import org.origin.common.rest.client.AbstractWSClient;
import org.origin.common.rest.client.ClientConfiguration;

public class GAIAWSClient extends AbstractWSClient {

	public GAIAWSClient(ClientConfiguration config) {
		super(config);
	}

	@Override
	public Builder updateHeaders(Builder builder) {
		return super.updateHeaders(builder);
	}

}
