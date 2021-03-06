package org.orbit.component.runtime.tier1.identity.service;

import org.orbit.component.runtime.model.identity.LoginRequest;
import org.orbit.component.runtime.model.identity.LoginResponse;
import org.orbit.component.runtime.model.identity.LogoutRequest;
import org.orbit.component.runtime.model.identity.LogoutResponse;
import org.orbit.component.runtime.model.identity.RefreshTokenResponse;
import org.orbit.component.runtime.model.identity.RegisterRequest;
import org.orbit.component.runtime.model.identity.RegisterResponse;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.AccessTokenProvider;
import org.origin.common.service.IWebService;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface IdentityService extends IWebService, AccessTokenProvider {

	boolean usernameExists(String username) throws ServerException;

	boolean emailExists(String email) throws ServerException;

	RegisterResponse register(RegisterRequest request) throws ServerException;

	LoginResponse login(LoginRequest request) throws ServerException;

	RefreshTokenResponse refreshToken(String refreshToken) throws ServerException;

	LogoutResponse logout(LogoutRequest request) throws ServerException;

}
