package org.orbit.component.connector.tier1.account;

import java.util.Map;

import org.orbit.component.api.tier1.account.UserAccounts;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class UserRegistryConnector extends ServiceConnector<UserAccounts> implements ConnectorActivator {

	public static final String ID = "org.orbit.component.connector.UserRegistryConnector";

	public UserRegistryConnector() {
		super(UserAccounts.class);
	}

	@Override
	protected UserAccounts create(Map<String, Object> properties) {
		return new UserRegistryImpl(this, properties);
	}

}
