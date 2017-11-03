package org.orbit.component.api.tier1.auth;

import java.util.Map;

import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.origin.common.rest.client.ClientException;

public interface Auth {

	/**
	 * Grant type constants
	 * 
	 * "client_credentials" for app login
	 * 
	 * "user_credentials" for user login
	 * 
	 * "refresh_token" for refreshing token
	 */
	public static final String GRANT_TYPE__CLIENT_CREDENTIALS = "client_credentials"; //$NON-NLS-1$
	public static final String GRANT_TYPE__USER_CREDENTIALS = "user_credentials"; //$NON-NLS-1$
	public static final String GRANT_TYPE__REFRESH_TOKEN = "refresh_token"; //$NON-NLS-1$

	void update(Map<String, Object> properties);

	String getName();

	String getURL();

	boolean ping();

	AuthorizationResponse authorize(AuthorizationRequest request) throws ClientException;

	TokenResponse token(TokenRequest tokenRequest) throws ClientException;

}
