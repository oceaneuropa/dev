package org.orbit.component.connector.tier1.account;

import java.util.Map;

import org.orbit.component.api.tier1.account.UserAccountClient;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class UserAccountConnector extends ServiceConnector<UserAccountClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.component.connector.UserRegistryConnector";

	public UserAccountConnector() {
		super(UserAccountClient.class);
	}

	@Override
	protected UserAccountClient create(Map<String, Object> properties) {
		return new UserAccountClientImpl(this, properties);
	}

}
