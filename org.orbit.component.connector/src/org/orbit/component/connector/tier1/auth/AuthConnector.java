package org.orbit.component.connector.tier1.auth;

import java.util.Map;

import org.orbit.component.api.tier1.auth.Auth;
import org.orbit.platform.sdk.connector.IConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class AuthConnector extends ServiceConnector<Auth> implements IConnectorActivator {

	public static final String ID = "org.orbit.component.connector.AuthConnector";

	public AuthConnector() {
		super(Auth.class);
	}

	@Override
	protected Auth create(Map<String, Object> properties) {
		return new AuthImpl(this, properties);
	}

}
