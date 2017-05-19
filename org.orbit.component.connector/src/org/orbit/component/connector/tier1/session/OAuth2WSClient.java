package org.orbit.component.connector.tier1.session;

import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;

/*
 * OAuth2 web service client.
 * 
 * {contextRoot} example: 
 * /orbit/v1/sso/oauth2
 * 
 * 
 * 
 */
public class OAuth2WSClient extends AbstractClient {

	public OAuth2WSClient(ClientConfiguration config) {
		super(config);
	}

}
