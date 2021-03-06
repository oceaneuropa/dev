package org.orbit.component.api.tier1.identity;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface IdentityServiceClient extends ServiceClient {

	boolean usernameExists(String username) throws ClientException;

	boolean emailExists(String email) throws ClientException;

	LoginResponse login(LoginRequest request) throws ClientException;

	CreateNewAccountResponse createNewAccount(CreateNewAccountRequest request) throws ClientException;

	RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws ClientException;

	LogoutResponse logout(LogoutRequest request) throws ClientException;

}
