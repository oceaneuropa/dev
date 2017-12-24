package org.orbit.component.connector.tier1.auth;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.tier1.auth.Auth;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.model.tier1.auth.AuthConverter;
import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.orbit.component.model.tier1.auth.dto.AuthorizationRequestDTO;
import org.orbit.component.model.tier1.auth.dto.AuthorizationResponseDTO;
import org.orbit.component.model.tier1.auth.dto.TokenRequestDTO;
import org.orbit.component.model.tier1.auth.dto.TokenResponseDTO;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;

public class AuthImpl implements Auth {

	protected Map<String, Object> properties;
	protected AuthWSClient client;

	/**
	 * 
	 * @param properties
	 */
	public AuthImpl(Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		initClient();
	}

	protected void initClient() {
		String realm = (String) properties.get(OrbitConstants.REALM);
		String username = (String) properties.get(OrbitConstants.USERNAME);
		String fullUrl = (String) properties.get(OrbitConstants.URL);

		String url = (String) properties.get(OrbitConstants.AUTH_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.AUTH_CONTEXT_ROOT);

		ClientConfiguration clientConfig = null;
		if (fullUrl != null) {
			clientConfig = ClientConfiguration.create(realm, username, fullUrl, null);
		} else {
			clientConfig = ClientConfiguration.create(realm, username, url, contextRoot);
		}

		this.client = new AuthWSClient(clientConfig);
	}

	@Override
	public void update(Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		initClient();
	}

	protected Map<String, Object> checkProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(OrbitConstants.AUTH_NAME);
		return name;
	}

	@Override
	public String getURL() {
		String fullUrl = (String) properties.get(OrbitConstants.URL);
		if (fullUrl != null) {
			return fullUrl;
		}

		String hostURL = (String) this.properties.get(OrbitConstants.AUTH_HOST_URL);
		String contextRoot = (String) this.properties.get(OrbitConstants.AUTH_CONTEXT_ROOT);
		return hostURL + contextRoot;
	}

	@Override
	public boolean ping() {
		return this.client.doPing();
	}

	@Override
	public String echo(String message) throws ClientException {
		return this.client.echo(message);
	}

	@Override
	public AuthorizationResponse authorize(AuthorizationRequest authRequest) throws ClientException {
		AuthorizationResponse authResponse = null;
		AuthorizationRequestDTO authRequestDTO = AuthConverter.getInstance().toRequestDTO(authRequest);
		try {
			AuthorizationResponseDTO authResponseDTO = this.client.authorize(authRequestDTO);
			if (authResponseDTO != null) {
				authResponse = AuthConverter.getInstance().toResponseDTO(authResponseDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return authResponse;
	}

	@Override
	public TokenResponse getToken(TokenRequest tokenRequest) throws ClientException {
		TokenResponse tokenResponse = null;
		TokenRequestDTO tokenRequestDTO = AuthConverter.getInstance().toRequestDTO(tokenRequest);
		try {
			TokenResponseDTO tokenResponseDTO = this.client.token(tokenRequestDTO);
			if (tokenResponseDTO != null) {
				tokenResponse = AuthConverter.getInstance().toResponseDTO(tokenResponseDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return tokenResponse;
	}

}
