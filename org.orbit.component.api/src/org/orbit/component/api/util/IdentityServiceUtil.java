package org.orbit.component.api.util;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.tier1.identity.IdentityServiceClient;
import org.orbit.component.api.tier1.identity.LoginRequest;
import org.orbit.component.api.tier1.identity.LoginResponse;
import org.orbit.component.api.tier1.identity.LogoutRequest;
import org.orbit.component.api.tier1.identity.LogoutResponse;
import org.orbit.component.api.tier1.identity.RefreshTokenRequest;
import org.orbit.component.api.tier1.identity.RefreshTokenResponse;
import org.orbit.component.api.tier1.identity.RegisterRequest;
import org.orbit.component.api.tier1.identity.RegisterResponse;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClientConstants;

public class IdentityServiceUtil {

	/**
	 * 
	 * @param identityServiceUrl
	 * @param accessToken
	 * @return
	 */
	public static IdentityServiceClient getClient(String identityServiceUrl, String accessToken) {
		IdentityServiceClient identityClient = null;
		if (identityServiceUrl != null) {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(WSClientConstants.REALM, null);
			properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
			properties.put(WSClientConstants.URL, identityServiceUrl);
			identityClient = ComponentClients.getInstance().getIdentityClient(properties);
		}
		return identityClient;
	}

	/**
	 * 
	 * @param identityServiceUrl
	 * @param username
	 * @param email
	 * @param password
	 * @return
	 * @throws ClientException
	 */
	public static RegisterResponse register(String identityServiceUrl, String username, String email, String password) throws ClientException {
		RegisterResponse response = null;
		IdentityServiceClient identityServiceClient = getClient(identityServiceUrl, username);
		if (identityServiceClient != null) {
			RegisterRequest request = new RegisterRequest(username, email, password);
			response = identityServiceClient.register(request);
		}
		return response;
	}

	/**
	 * 
	 * @param identityServiceUrl
	 * @param username
	 * @param email
	 * @param password
	 * @return
	 * @throws ClientException
	 */
	public static LoginResponse login(String identityServiceUrl, String username, String email, String password) throws ClientException {
		LoginResponse response = null;
		IdentityServiceClient identityServiceClient = getClient(identityServiceUrl, username);
		if (identityServiceClient != null) {
			LoginRequest request = new LoginRequest("orbit", "password", username, email, password);
			response = identityServiceClient.login(request);
		}
		return response;
	}

	/**
	 * 
	 * @param identityServiceUrl
	 * @param refreshToken
	 * @return
	 * @throws ClientException
	 */
	public static RefreshTokenResponse refreshToken(String identityServiceUrl, String refreshToken) throws ClientException {
		RefreshTokenResponse response = null;
		IdentityServiceClient identityServiceClient = getClient(identityServiceUrl, refreshToken);
		if (identityServiceClient != null && refreshToken != null) {
			response = identityServiceClient.refreshToken(new RefreshTokenRequest(refreshToken));
		}
		return response;
	}

	/**
	 * 
	 * @param identityServiceUrl
	 * @param username
	 * @param email
	 * @param password
	 * @return
	 * @throws ClientException
	 */
	public static LogoutResponse logout(String identityServiceUrl, String tokenType, String accessToken, String refreshToken) throws ClientException {
		LogoutResponse response = null;
		IdentityServiceClient identityServiceClient = getClient(identityServiceUrl, accessToken);
		if (identityServiceClient != null) {
			LogoutRequest request = new LogoutRequest(tokenType, accessToken, refreshToken);
			response = identityServiceClient.logout(request);
		}
		return response;
	}

}
