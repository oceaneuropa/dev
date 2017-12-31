package org.orbit.component.api.tier1.auth;

import java.util.Map;

import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;

public interface Auth extends IAdaptable {

	Map<String, Object> getProperties();

	void update(Map<String, Object> properties);

	String getName();

	String getURL();

	boolean ping();

	String echo(String message) throws ClientException;

	AuthorizationResponse authorize(AuthorizationRequest request) throws ClientException;

	TokenResponse getToken(TokenRequest tokenRequest) throws ClientException;

	boolean close() throws ClientException;

}
