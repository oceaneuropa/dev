package org.orbit.component.connector.tier1.account;

import java.util.Map;

import org.orbit.component.api.tier1.account.UserRegistry;
import org.origin.common.rest.client.ServiceConnector;

public class UserRegistryConnectorImpl extends ServiceConnector<UserRegistry> {

	public UserRegistryConnectorImpl() {
		super(UserRegistry.class);
	}

	@Override
	protected UserRegistry create(Map<String, Object> properties) {
		return new UserRegistryImpl(this, properties);
	}

}
