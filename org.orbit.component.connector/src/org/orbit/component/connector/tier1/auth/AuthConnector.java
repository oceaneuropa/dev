package org.orbit.component.connector.tier1.auth;

import java.util.Map;

import org.orbit.component.api.tier1.auth.Auth;
import org.origin.common.rest.client.ServiceConnector;

public class AuthConnector extends ServiceConnector<Auth> {

	public AuthConnector() {
		super(Auth.class);
	}

	@Override
	protected Auth create(Map<String, Object> properties) {
		return new AuthImpl(this, properties);
	}

}
