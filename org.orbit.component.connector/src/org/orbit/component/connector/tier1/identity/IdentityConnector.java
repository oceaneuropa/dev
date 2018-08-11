package org.orbit.component.connector.tier1.identity;

import java.util.Map;

import org.orbit.component.api.tier1.identity.IdentityClient;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class IdentityConnector extends ServiceConnector<IdentityClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.component.connector.IdentityConnector";

	public IdentityConnector() {
		super(IdentityClient.class);
	}

	@Override
	protected IdentityClient create(Map<String, Object> properties) {
		return new IdentityClientImpl(this, properties);
	}

}
