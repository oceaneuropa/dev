package org.orbit.component.connector.tier1.session;

import org.origin.common.rest.client.WSClient;
import org.origin.common.rest.client.WSClientConfiguration;

/*
 * OAuth2 web service client.
 * 
 * {contextRoot} example: 
 * /orbit/v1/sso/oauth2
 * 
 * 
 */
public class OAuth2WSClient extends WSClient {

	public OAuth2WSClient(WSClientConfiguration config) {
		super(config);
	}

}
