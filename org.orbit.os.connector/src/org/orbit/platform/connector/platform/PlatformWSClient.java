package org.orbit.platform.connector.platform;

import javax.ws.rs.client.Invocation.Builder;

import org.origin.common.rest.client.AbstractWSClient;
import org.origin.common.rest.client.ClientConfiguration;

public class PlatformWSClient extends AbstractWSClient {

	public PlatformWSClient(ClientConfiguration config) {
		super(config);
	}

	@Override
	public Builder updateHeaders(Builder builder) {
		return super.updateHeaders(builder);
	}

}
