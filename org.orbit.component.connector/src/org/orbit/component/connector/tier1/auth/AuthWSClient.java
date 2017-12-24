package org.orbit.component.connector.tier1.auth;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.model.tier1.auth.dto.AuthorizationRequestDTO;
import org.orbit.component.model.tier1.auth.dto.AuthorizationResponseDTO;
import org.orbit.component.model.tier1.auth.dto.TokenRequestDTO;
import org.orbit.component.model.tier1.auth.dto.TokenResponseDTO;
import org.origin.common.rest.client.AbstractWSClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;

/*
 * Auth web service client.
 * 
 * {contextRoot} example: /orbit/v1/auth
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/echo/{message}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/authorize (Body parameter: AuthorizationRequestDTO)
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/token (Body parameter: TokenRequestDTO)
 * 
 * @see TransferAgentServiceResource
 * 
 */
public class AuthWSClient extends AbstractWSClient {

	public AuthWSClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/echo?message={message}
	 * 
	 * @param message
	 * @return
	 * @throws ClientException
	 */
	public String echo(String message) throws ClientException {
		String result = null;
		try {
			WebTarget target = getRootPath().path("echo").queryParam("message", message);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(target, response);

			result = response.readEntity(String.class);

		} catch (ClientException e) {
			handleException(e);
		}
		return result;
	}

	/**
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/authorize (Body parameter: AuthorizationRequestDTO)
	 * 
	 * @param request
	 * @return
	 * @throws ClientException
	 */
	public AuthorizationResponseDTO authorize(AuthorizationRequestDTO request) throws ClientException {
		AuthorizationResponseDTO authResponse = null;
		try {
			Entity<?> bodyParam = Entity.json(new GenericEntity<AuthorizationRequestDTO>(request) {
			});

			WebTarget target = getRootPath().path("authorize");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(bodyParam);
			checkResponse(target, response);

			// String responseString = response.readEntity(String.class);
			// int responseStatus = response.getStatus();
			// System.out.println("responseString = " + responseString);
			// System.out.println("responseStatus = " + responseStatus);
			authResponse = response.readEntity(AuthorizationResponseDTO.class);

		} catch (ClientException e) {
			handleException(e);
		}
		return authResponse;
	}

	/**
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/token (Body parameter: TokenRequestDTO)
	 * 
	 * @param request
	 * @return
	 * @throws ClientException
	 */
	public TokenResponseDTO token(TokenRequestDTO request) throws ClientException {
		TokenResponseDTO tokenResponse = null;
		try {
			Entity<?> bodyParam = Entity.json(new GenericEntity<TokenRequestDTO>(request) {
			});

			WebTarget target = getRootPath().path("token");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(bodyParam);
			checkResponse(target, response);

			tokenResponse = response.readEntity(TokenResponseDTO.class);

			if (tokenResponse != null) {
				String tokenType = tokenResponse.getToken_type();
				String accessToken = tokenResponse.getAccess_token();
				if (accessToken != null) {
					updateToken(tokenType, accessToken);
				}
			}

		} catch (ClientException e) {
			handleException(e);
		}
		return tokenResponse;
	}

}
