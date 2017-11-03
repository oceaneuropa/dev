package org.orbit.component.server.tier1.auth.service;

import org.orbit.component.model.tier1.auth.AuthException;
import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenResponse;

public interface AuthService {

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

	/**
	 * 
	 * @return
	 */
	String getNamespace();

	/**
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 
	 * @return
	 */
	String getHostURL();

	/**
	 * 
	 * @return
	 */
	String getContextRoot();

	/**
	 * 
	 * @param request
	 * @return
	 * @throws AuthException
	 */
	AuthorizationResponse onAuthorize(AuthorizationRequest request) throws AuthException;

	/**
	 * 
	 * @param request
	 * @return
	 * @throws AuthException
	 */
	TokenResponse onToken(TokenRequest request) throws AuthException;

}
