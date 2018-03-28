package org.orbit.component.connector.tier1.auth;

import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationRequestDTO;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.AuthorizationResponseDTO;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenRequestDTO;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.orbit.component.model.tier1.auth.TokenResponseDTO;

public class AuthModelConverter {

	protected static AuthModelConverter INSTANCE = new AuthModelConverter();

	public static AuthModelConverter getInstance() {
		return INSTANCE;
	}

	// ----------------------------------------------------------------------
	// Use on client side
	// ----------------------------------------------------------------------
	/**
	 * Convert AuthorizationRequest object to AuthorizationRequestDTO object.
	 * 
	 * @param authorizationRequest
	 * @return
	 */
	public AuthorizationRequestDTO toRequestDTO(AuthorizationRequest authorizationRequest) {
		String client_id = authorizationRequest.getClient_id();
		String response_type = authorizationRequest.getResponse_type();
		String redirect_url = authorizationRequest.getRedirect_url();
		String scope = authorizationRequest.getScope();
		String state = authorizationRequest.getState();

		AuthorizationRequestDTO requestDTO = new AuthorizationRequestDTO();
		requestDTO.setClient_id(client_id);
		requestDTO.setResponse_type(response_type);
		requestDTO.setRedirect_url(redirect_url);
		requestDTO.setScope(scope);
		requestDTO.setState(state);

		return requestDTO;
	}

	/**
	 * Convert AuthorizationResponseDTO object to AuthorizationResponse object.
	 * 
	 * @param authorizationResponseDTO
	 * @return
	 */
	public AuthorizationResponse toResponseDTO(AuthorizationResponseDTO authorizationResponseDTO) {
		String client_id = authorizationResponseDTO.getClient_id();
		String response_type = authorizationResponseDTO.getResponse_type();
		String redirect_url = authorizationResponseDTO.getRedirect_url();
		String scope = authorizationResponseDTO.getScope();
		String state = authorizationResponseDTO.getState();

		AuthorizationResponse response = new AuthorizationResponse();
		response.setClient_id(client_id);
		response.setResponse_type(response_type);
		response.setRedirect_url(redirect_url);
		response.setScope(scope);
		response.setState(state);

		return response;
	}

	/**
	 * Convert TokenRequest object to TokenRequestDTO object.
	 * 
	 * @param tokenRequest
	 * @return
	 */
	public TokenRequestDTO toRequestDTO(TokenRequest tokenRequest) {
		String grant_type = tokenRequest.getGrantType();
		String client_id = tokenRequest.getClientId();
		String client_secret = tokenRequest.getClient_secret();
		String username = tokenRequest.getUsername();
		String password = tokenRequest.getPassword();
		String refresh_token = tokenRequest.getRefreshToken();
		String scope = tokenRequest.getScope();
		String state = tokenRequest.getState();

		TokenRequestDTO requestDTO = new TokenRequestDTO();
		requestDTO.setGrant_type(grant_type);
		requestDTO.setClient_id(client_id);
		requestDTO.setClient_secret(client_secret);
		requestDTO.setUsername(username);
		requestDTO.setPassword(password);
		requestDTO.setRefresh_token(refresh_token);
		requestDTO.setScope(scope);
		requestDTO.setState(state);

		return requestDTO;
	}

	/**
	 * Convert TokenResponseDTO object to TokenResponse object.
	 * 
	 * @param tokenResponseDTO
	 * @return
	 */
	public TokenResponse toResponseDTO(TokenResponseDTO tokenResponseDTO) {
		String token_type = tokenResponseDTO.getToken_type();
		String access_token = tokenResponseDTO.getAccess_token();
		long expires_in = tokenResponseDTO.getExpires_in();
		String refresh_token = tokenResponseDTO.getRefresh_token();
		String scope = tokenResponseDTO.getScope();
		String state = tokenResponseDTO.getState();

		TokenResponse response = new TokenResponse();
		response.setTokenType(token_type);
		response.setAccessToken(access_token);
		response.setExpiresIn(expires_in);
		response.setRefreshToken(refresh_token);
		response.setScope(scope);
		response.setState(state);

		return response;
	}

	// ----------------------------------------------------------------------
	// Use on server side
	// ----------------------------------------------------------------------
	// /**
	// * Convert ServerException object to ErrorResponseDTO object.
	// *
	// * @param e
	// * @return
	// */
	// public ErrorResponseDTO toResponseDTO(ServerException e) {
	// String error = e.getError();
	// String error_description = e.getError_description();
	// String error_url = e.getError_url();
	//
	// ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
	// errorResponseDTO.setError(error);
	// errorResponseDTO.setError_description(error_description);
	// errorResponseDTO.setError_uri(error_url);
	//
	// return errorResponseDTO;
	// }

	/**
	 * Convert AuthorizationRequestDTO object to AuthorizationRequest object.
	 * 
	 * @param authorizationRequestDTO
	 * @return
	 */
	public AuthorizationRequest toRequest(AuthorizationRequestDTO authorizationRequestDTO) {
		String client_id = authorizationRequestDTO.getClient_id();
		String response_type = authorizationRequestDTO.getResponse_type();
		String redirect_url = authorizationRequestDTO.getRedirect_url();
		String scope = authorizationRequestDTO.getScope();
		String state = authorizationRequestDTO.getState();

		AuthorizationRequest request = new AuthorizationRequest();
		request.setClient_id(client_id);
		request.setResponse_type(response_type);
		request.setRedirect_url(redirect_url);
		request.setScope(scope);
		request.setState(state);

		return request;
	}

	/**
	 * Convert AuthorizationResponse object to AuthorizationResponseDTO object.
	 * 
	 * @param authorizationResponse
	 * @return
	 */
	public AuthorizationResponseDTO toResponseDTO(AuthorizationResponse authorizationResponse) {
		String client_id = authorizationResponse.getClient_id();
		String response_type = authorizationResponse.getResponse_type();
		String redirect_url = authorizationResponse.getRedirect_url();
		String scope = authorizationResponse.getScope();
		String state = authorizationResponse.getState();

		AuthorizationResponseDTO responseDTO = new AuthorizationResponseDTO();
		responseDTO.setClient_id(client_id);
		responseDTO.setResponse_type(response_type);
		responseDTO.setRedirect_url(redirect_url);
		responseDTO.setScope(scope);
		responseDTO.setState(state);

		return responseDTO;
	}

	/**
	 * Convert TokenRequestDTO object to TokenRequest object.
	 * 
	 * @param tokenRequestDTO
	 * @return
	 */
	public TokenRequest toRequest(TokenRequestDTO tokenRequestDTO) {
		String grant_type = tokenRequestDTO.getGrant_type();
		String client_id = tokenRequestDTO.getClient_id();
		String client_secret = tokenRequestDTO.getClient_secret();
		String username = tokenRequestDTO.getUsername();
		String password = tokenRequestDTO.getPassword();
		String refresh_token = tokenRequestDTO.getRefresh_token();
		String scope = tokenRequestDTO.getScope();
		String state = tokenRequestDTO.getState();

		TokenRequest request = new TokenRequest();
		request.setGrantType(grant_type);
		request.setClientId(client_id);
		request.setClientSecret(client_secret);
		request.setUsername(username);
		request.setPassword(password);
		request.setRefreshToken(refresh_token);
		request.setScope(scope);
		request.setState(state);

		return request;
	}

	/**
	 * Convert TokenResponse object to TokenResponseDTO object.
	 * 
	 * @param tokenResponse
	 * @return
	 */
	public TokenResponseDTO toResponseDTO(TokenResponse tokenResponse) {
		String token_type = tokenResponse.getTokenType();
		String access_token = tokenResponse.getAccessToken();
		long expires_in = tokenResponse.getExpiresIn();
		String refresh_token = tokenResponse.getRefreshToken();
		String scope = tokenResponse.getScope();
		String state = tokenResponse.getState();

		TokenResponseDTO responseDTO = new TokenResponseDTO();
		responseDTO.setToken_type(token_type);
		responseDTO.setAccess_token(access_token);
		responseDTO.setExpires_in(expires_in);
		responseDTO.setRefresh_token(refresh_token);
		responseDTO.setScope(scope);
		responseDTO.setState(state);

		return responseDTO;
	}

}
