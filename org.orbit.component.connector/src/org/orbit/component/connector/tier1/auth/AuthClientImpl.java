package org.orbit.component.connector.tier1.auth;

import java.util.Map;

import org.orbit.component.api.tier1.auth.AuthClient;
import org.orbit.component.connector.util.ClientModelConverter;
import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationRequestDTO;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.AuthorizationResponseDTO;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenRequestDTO;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.orbit.component.model.tier1.auth.TokenResponseDTO;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;

public class AuthClientImpl extends ServiceClientImpl<AuthClient, AuthWSClient> implements AuthClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public AuthClientImpl(ServiceConnector<AuthClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected AuthWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration clientConfig = WSClientConfiguration.create(properties);
		return new AuthWSClient(clientConfig);
	}

	@Override
	public AuthorizationResponse authorize(AuthorizationRequest authRequest) throws ClientException {
		AuthorizationResponse authResponse = null;
		AuthorizationRequestDTO authRequestDTO = ClientModelConverter.Auth.toRequestDTO(authRequest);
		try {
			AuthorizationResponseDTO authResponseDTO = this.client.authorize(authRequestDTO);
			if (authResponseDTO != null) {
				authResponse = ClientModelConverter.Auth.toResponseDTO(authResponseDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return authResponse;
	}

	@Override
	public TokenResponse getToken(TokenRequest tokenRequest) throws ClientException {
		TokenResponse tokenResponse = null;
		TokenRequestDTO tokenRequestDTO = ClientModelConverter.Auth.toRequestDTO(tokenRequest);
		try {
			TokenResponseDTO tokenResponseDTO = this.client.token(tokenRequestDTO);
			if (tokenResponseDTO != null) {
				tokenResponse = ClientModelConverter.Auth.toResponseDTO(tokenResponseDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return tokenResponse;
	}

}
