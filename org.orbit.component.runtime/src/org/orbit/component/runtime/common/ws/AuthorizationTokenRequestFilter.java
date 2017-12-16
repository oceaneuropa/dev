package org.orbit.component.runtime.common.ws;

import java.io.IOException;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import org.origin.common.util.JWTUtil;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @see https://tools.ietf.org/html/rfc6750
 * 
 *      The OAuth 2.0 Authorization Framework: Bearer Token Usage
 * 
 * @see org.origin.common.rest.filter.BearerTokenFilter
 * 
 */
public class AuthorizationTokenRequestFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String headerValue = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		// if (authHeader == null) {
		// throw new NotAuthorizedException("Bearer");
		// }

		String token = parseToken(headerValue);
		DecodedJWT jwt = verifyToken(token);
		if (jwt == null) {
			throw new NotAuthorizedException("Bearer error=\"invalid_token\"");
		}

		if (jwt != null) {
			// JWTUtil.print(jwt);

			Claim username = jwt.getClaim("username");
			Claim email = jwt.getClaim("email");
			Claim firstName = jwt.getClaim("firstName");
			Claim lastName = jwt.getClaim("lastName");

			System.out.println("username = " + (username != null ? username.asString() : null));
			System.out.println("email = " + (email != null ? email.asString() : null));
			System.out.println("firstName = " + (firstName != null ? firstName.asString() : null));
			System.out.println("lastName = " + (lastName != null ? lastName.asString() : null));
		}
	}

	protected String parseToken(String headerValue) {
		if (headerValue != null) {
			int index = headerValue.indexOf(" ");
			if (index > 0) {
				String tokenType = headerValue.substring(0, index);
				String accessToken = headerValue.substring(index + 1);
				if ("Bearer".equalsIgnoreCase(tokenType)) {
					return accessToken;
				}
			}
		}
		return null;
	}

	protected DecodedJWT verifyToken(String token) {
		DecodedJWT jwt = null;
		if (token != null) {
			try {
				jwt = JWTUtil.verifyToken("Einstein", "orbit.auth", token);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jwt;
	}

}
