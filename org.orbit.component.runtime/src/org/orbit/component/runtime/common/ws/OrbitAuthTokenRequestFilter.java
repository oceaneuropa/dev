package org.orbit.component.runtime.common.ws;

import java.io.IOException;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import org.origin.common.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class OrbitAuthTokenRequestFilter implements ContainerRequestFilter {

	protected static Logger LOG = LoggerFactory.getLogger(OrbitAuthTokenRequestFilter.class);
	protected boolean enabled = true;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String headerValue = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (headerValue == null) {
			if (enabled) {
				throw new NotAuthorizedException("Bearer");
			}
		}

		String token = parseToken(headerValue);
		DecodedJWT jwt = verifyJWT(token);
		if (jwt == null) {
			if (enabled) {
				throw new NotAuthorizedException("Bearer error=\"invalid_token\"");
			}
		}

		String username = null;
		if (jwt != null) {
			// JWTUtil.print(jwt);

			Claim usernameClaim = jwt.getClaim("username");
			Claim emailClaim = jwt.getClaim("email");
			Claim firstNameClaim = jwt.getClaim("firstName");
			Claim lastNameClaim = jwt.getClaim("lastName");

			if (usernameClaim != null) {
				username = usernameClaim.asString();
			}
		}

		LOG.info("username = " + username);
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

	protected DecodedJWT verifyJWT(String token) {
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
