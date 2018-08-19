package org.orbit.spirit.connector.gaia;

import javax.ws.rs.client.Invocation.Builder;

import org.origin.common.rest.client.WSClient;
import org.origin.common.rest.client.WSClientConfiguration;

public class GAIAWSClient extends WSClient {

	public GAIAWSClient(WSClientConfiguration config) {
		super(config);
	}

	@Override
	public Builder updateHeaders(Builder builder) {
		return super.updateHeaders(builder);
	}

}
