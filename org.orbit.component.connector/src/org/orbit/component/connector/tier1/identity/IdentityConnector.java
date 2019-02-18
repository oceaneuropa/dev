package org.orbit.component.connector.tier1.identity;

import java.util.Map;

import org.orbit.component.api.tier1.identity.IdentityServiceClient;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class IdentityConnector extends ServiceConnector<IdentityServiceClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.component.connector.IdentityConnector";

	public IdentityConnector() {
		super(IdentityServiceClient.class);
	}

	@Override
	protected IdentityServiceClient create(Map<String, Object> properties) {
		return new IdentityServiceClientImpl(this, properties);
	}

}
