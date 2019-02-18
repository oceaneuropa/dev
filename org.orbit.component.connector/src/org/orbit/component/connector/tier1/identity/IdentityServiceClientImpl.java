package org.orbit.component.connector.tier1.identity;

import java.util.Map;

import org.orbit.component.api.tier1.identity.IdentityServiceClient;
import org.orbit.component.api.tier1.identity.LoginRequest;
import org.orbit.component.api.tier1.identity.LoginResponse;
import org.orbit.component.api.tier1.identity.LogoutRequest;
import org.orbit.component.api.tier1.identity.LogoutResponse;
import org.orbit.component.api.tier1.identity.RegisterRequest;
import org.orbit.component.api.tier1.identity.RegisterResponse;
import org.orbit.component.connector.util.ModelConverter;
import org.orbit.component.model.tier1.identity.LoginRequestDTO;
import org.orbit.component.model.tier1.identity.LoginResponseDTO;
import org.orbit.component.model.tier1.identity.LogoutRequestDTO;
import org.orbit.component.model.tier1.identity.RegisterRequestDTO;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.StatusDTO;

public class IdentityServiceClientImpl extends ServiceClientImpl<IdentityServiceClient, IdentityWSClient> implements IdentityServiceClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public IdentityServiceClientImpl(IdentityConnector connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected IdentityWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new IdentityWSClient(config);
	}

	@Override
	public boolean usernameExists(String username) throws ClientException {
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("username is empty");
		}
		boolean exists = getWSClient().exists("username", username);
		return exists;
	}

	@Override
	public boolean emailExists(String email) throws ClientException {
		if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("email is empty");
		}
		boolean exists = getWSClient().exists("email", email);
		return exists;
	}

	@Override
	public RegisterResponse register(RegisterRequest request) throws ClientException {
		if (request == null) {
			throw new IllegalArgumentException("request is null");
		}
		RegisterResponse response = new RegisterResponse();
		RegisterRequestDTO requestDTO = ModelConverter.Identity.toRequestDTO(request);
		StatusDTO status = getWSClient().register(requestDTO);
		if (status != null) {
			boolean success = status.success();
			String message = status.getMessage();
			response.setSucceed(success);
			response.setMessage(message);
		}
		return response;
	}

	@Override
	public LoginResponse login(LoginRequest request) throws ClientException {
		if (request == null) {
			throw new IllegalArgumentException("request is null");
		}
		LoginResponse response = new LoginResponse();
		LoginRequestDTO requestDTO = ModelConverter.Identity.toRequestDTO(request);
		LoginResponseDTO responseDTO = getWSClient().login(requestDTO);
		if (responseDTO != null) {
			LoginResponse theResponse = ModelConverter.Identity.toResponse(responseDTO);
			if (theResponse != null) {
				response = theResponse;
			}
		}
		return response;
	}

	@Override
	public LogoutResponse logout(LogoutRequest request) throws ClientException {
		if (request == null) {
			throw new IllegalArgumentException("request is null");
		}
		LogoutResponse response = new LogoutResponse();
		LogoutRequestDTO requestDTO = ModelConverter.Identity.toRequestDTO(request);
		StatusDTO status = getWSClient().logout(requestDTO);
		if (status != null) {
			boolean success = status.success();
			String message = status.getMessage();
			response.setSucceed(success);
			response.setMessage(message);
		}
		return response;
	}

}
