package org.orbit.component.connector.tier1.auth;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.tier1.auth.AuthClient;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.connector.util.ModelConverter;
import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationRequestDTO;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.AuthorizationResponseDTO;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenRequestDTO;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.orbit.component.model.tier1.auth.TokenResponseDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceConnector;

public class AuthClientImpl implements AuthClient {

	protected Map<String, Object> properties;
	protected AuthWSClient client;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public AuthClientImpl(ServiceConnector<AuthClient> connector, Map<String, Object> properties) {
		if (connector != null) {
			adapt(ServiceConnector.class, connector);
		}
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
	public boolean close() throws ClientException {
		@SuppressWarnings("unchecked")
		ServiceConnector<AuthClient> connector = getAdapter(ServiceConnector.class);
		if (connector != null) {
			return connector.close(this);
		}
		return false;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	@Override
	public void update(Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		initClient();
	}

	protected void initClient() {
		String realm = (String) properties.get(OrbitConstants.REALM);
		String username = (String) properties.get(OrbitConstants.USERNAME);
		String fullUrl = (String) properties.get(OrbitConstants.URL);

		ClientConfiguration clientConfig = ClientConfiguration.create(realm, username, fullUrl);
		this.client = new AuthWSClient(clientConfig);
	}

	@Override
	public String getURL() {
		String fullUrl = (String) properties.get(OrbitConstants.URL);
		return fullUrl;
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
		AuthorizationRequestDTO authRequestDTO = ModelConverter.Auth.toRequestDTO(authRequest);
		try {
			AuthorizationResponseDTO authResponseDTO = this.client.authorize(authRequestDTO);
			if (authResponseDTO != null) {
				authResponse = ModelConverter.Auth.toResponseDTO(authResponseDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return authResponse;
	}

	@Override
	public TokenResponse getToken(TokenRequest tokenRequest) throws ClientException {
		TokenResponse tokenResponse = null;
		TokenRequestDTO tokenRequestDTO = ModelConverter.Auth.toRequestDTO(tokenRequest);
		try {
			TokenResponseDTO tokenResponseDTO = this.client.token(tokenRequestDTO);
			if (tokenResponseDTO != null) {
				tokenResponse = ModelConverter.Auth.toResponseDTO(tokenResponseDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return tokenResponse;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

}
