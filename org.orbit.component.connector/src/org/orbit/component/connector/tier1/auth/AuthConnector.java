package org.orbit.component.connector.tier1.auth;

import java.util.Map;

import org.orbit.component.api.tier1.auth.AuthClient;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class AuthConnector extends ServiceConnector<AuthClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.component.connector.AuthConnector";

	public AuthConnector() {
		super(AuthClient.class);
	}

	@Override
	protected AuthClient create(Map<String, Object> properties) {
		return new AuthClientImpl(this, properties);
	}

}
