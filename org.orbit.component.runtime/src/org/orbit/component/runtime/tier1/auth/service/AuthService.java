package org.orbit.component.runtime.tier1.auth.service;

import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.AccessTokenProvider;
import org.origin.common.service.IWebService;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface AuthService extends IWebService, AccessTokenProvider {

	/**
	 * Grant type constants
	 * 
	 * "client_credentials" for app authorization. A third party app wants to access a resource (e.g. photos, contacts, etc.) of the user. This will be useful when apps are available.
	 * 
	 * "user_credentials" for user authorization
	 * 
	 * "refresh_token" for refreshing token
	 */
	public static final String GRANT_TYPE__CLIENT_CREDENTIALS = "client_credentials"; //$NON-NLS-1$
	public static final String GRANT_TYPE__USER_CREDENTIALS = "user_credentials"; //$NON-NLS-1$
	public static final String GRANT_TYPE__REFRESH_TOKEN = "refresh_token"; //$NON-NLS-1$

	AuthorizationResponse authorize(AuthorizationRequest request) throws ServerException;

	TokenResponse getToken(TokenRequest request) throws ServerException;

}
