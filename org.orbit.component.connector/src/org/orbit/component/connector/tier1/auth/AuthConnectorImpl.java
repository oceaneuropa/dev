package org.orbit.component.connector.tier1.auth;

import java.util.Map;

import org.orbit.component.api.tier1.auth.Auth;
import org.origin.common.rest.client.ServiceConnector;

public class AuthConnectorImpl extends ServiceConnector<Auth> {

	public AuthConnectorImpl() {
		super(Auth.class);
	}

	@Override
	protected Auth create(Map<String, Object> properties) {
		return new AuthImpl(properties);
	}

}
