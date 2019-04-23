package other.orbit.component.connector.tier1.auth;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.component.api.tier1.auth.AuthClient;
import org.orbit.component.connector.tier1.auth.AuthWSClient;
import org.orbit.component.connector.util.ClientModelConverter;
import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationRequestDTO;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.AuthorizationResponseDTO;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenRequestDTO;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.orbit.component.model.tier1.auth.TokenResponseDTO;
import org.orbit.infra.api.InfraConstants;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.ServiceMetadata;

public class AuthImplClientV1 implements AuthClient {

	protected Map<String, Object> properties;
	protected AuthWSClient client;

	/**
	 * 
	 * @param properties
	 */
	public AuthImplClientV1(Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		initClient();
	}

	protected void initClient() {
		String realm = (String) properties.get(WSClientConstants.REALM);
		String accessToken = (String) properties.get(WSClientConstants.ACCESS_TOKEN);
		String url = (String) properties.get(WSClientConstants.URL);

		// String url = (String) properties.get(OrbitConstants.AUTH_HOST_URL);
		// String contextRoot = (String) properties.get(OrbitConstants.AUTH_CONTEXT_ROOT);
		// ClientConfiguration clientConfig = null;
		// if (fullUrl != null) {
		// clientConfig = ClientConfiguration.create(realm, username, fullUrl, null);
		// } else {
		// clientConfig = ClientConfiguration.create(realm, username, url, contextRoot);
		// }

		WSClientConfiguration config = WSClientConfiguration.create(realm, accessToken, url, null);
		this.client = new AuthWSClient(config);
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

	protected Map<String, Object> checkProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	// @Override
	// public String getName() {
	// String name = (String) this.properties.get(OrbitConstants.AUTH_NAME);
	// return name;
	// }

	@Override
	public String getURL() {
		String url = (String) properties.get(WSClientConstants.URL);
		if (url != null) {
			return url;
		}

		String hostURL = (String) this.properties.get(InfraConstants.SERVICE__HOST_URL);
		String contextRoot = (String) this.properties.get(InfraConstants.SERVICE__CONTEXT_ROOT);
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

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {

	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public boolean close() throws ClientException {
		return false;
	}

	@Override
	public ServiceMetadata getMetadata() throws ClientException {
		return null;
	}

	@Override
	public String getName() throws ClientException {
		return null;
	}

	@Override
	public Response sendRequest(Request request) throws ClientException {
		return null;
	}

	@Override
	public boolean isProxy() {
		return false;
	}

}
