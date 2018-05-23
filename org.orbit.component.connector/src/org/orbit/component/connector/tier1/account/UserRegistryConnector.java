package org.orbit.component.connector.tier1.account;

import java.util.Map;

import org.orbit.component.api.tier1.account.UserRegistry;
import org.orbit.platform.sdk.connector.IConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class UserRegistryConnector extends ServiceConnector<UserRegistry> implements IConnectorActivator {

	public static final String ID = "org.orbit.component.connector.UserRegistryConnector";

	public UserRegistryConnector() {
		super(UserRegistry.class);
	}

	@Override
	protected UserRegistry create(Map<String, Object> properties) {
		return new UserRegistryImpl(this, properties);
	}

}
