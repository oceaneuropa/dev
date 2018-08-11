package org.orbit.component.api.tier1.identity;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface IdentityClient extends ServiceClient {

	boolean usernameExists(String username) throws ClientException;

	boolean emailExists(String email) throws ClientException;

	RegisterResponse register(RegisterRequest request) throws ClientException;

	LoginResponse login(LoginRequest request) throws ClientException;

	LogoutResponse logout(LogoutRequest request) throws ClientException;

}
