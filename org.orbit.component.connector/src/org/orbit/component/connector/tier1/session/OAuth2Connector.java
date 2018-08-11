package org.orbit.component.connector.tier1.session;

import java.util.Map;

import org.orbit.component.api.tier1.session.OAuth2Client;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.rest.client.ServiceConnector;

public class OAuth2Connector extends ServiceConnector<OAuth2Client> implements ConnectorActivator {

	public static final String ID = "org.orbit.component.connector.OAuth2Connector";

	public OAuth2Connector() {
		super(OAuth2Client.class);
	}

	@Override
	protected OAuth2Client create(Map<String, Object> properties) {
		return new OAuth2ClientImpl(this, properties);
	}

}
