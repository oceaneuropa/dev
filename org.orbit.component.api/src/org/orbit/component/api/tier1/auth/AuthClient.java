package org.orbit.component.api.tier1.auth;

import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface AuthClient extends ServiceClient {

	AuthorizationResponse authorize(AuthorizationRequest request) throws ClientException;

	TokenResponse getToken(TokenRequest tokenRequest) throws ClientException;

}
