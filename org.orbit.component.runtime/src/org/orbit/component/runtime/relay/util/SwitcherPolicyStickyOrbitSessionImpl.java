package org.orbit.component.runtime.relay.util;

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import org.origin.common.rest.switcher.impl.SwitcherPolicyStickyImpl;
import org.origin.common.util.JWTUtil;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class SwitcherPolicyStickyOrbitSessionImpl<URI> extends SwitcherPolicyStickyImpl<URI> {

	@Override
	protected String getClientId(ContainerRequestContext requestContext) {
		String username = null;
		String orbitSession = getOrbitSession(requestContext);
		if (orbitSession != null) {
			// extract userId from the session
			DecodedJWT jwt = verifyJWT(orbitSession);
			if (jwt != null) {
				Claim usernameClaim = jwt.getClaim("username");
				if (usernameClaim != null) {
					username = usernameClaim.asString();
				}
			}
		}
		return username;
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

	protected String getHttpAuthorization(ContainerRequestContext requestContext) {
		String httpAuthorization = null;
		MultivaluedMap<String, String> headers = requestContext.getHeaders();
		if (headers != null) {
			for (Iterator<String> headerItor = headers.keySet().iterator(); headerItor.hasNext();) {
				String headerName = headerItor.next();
				if (HttpHeaders.AUTHORIZATION.equals(headerName)) {
					httpAuthorization = requestContext.getHeaderString(headerName);
					break;
				}
			}
		}
		// System.out.println("httpAuthorization = " + httpAuthorization);
		return httpAuthorization;
	}

	protected String getOrbitSession(ContainerRequestContext requestContext) {
		String orbitSession = null;
		MultivaluedMap<String, String> headers = requestContext.getHeaders();
		if (headers != null) {
			for (Iterator<String> headerItor = headers.keySet().iterator(); headerItor.hasNext();) {
				String headerName = headerItor.next();
				if (HttpHeaders.COOKIE.equals(headerName)) {
					List<String> headerValues = headers.get(headerName);
					if (headerValues != null) {
						for (String headerValue : headerValues) {
							if (headerValue.contains("OrbitSession")) {
								int index = headerValue.indexOf("=");
								if (index > 0) {
									orbitSession = headerValue.substring(index + 1);
								}
							}
						}
					}
					break;
				}
			}
		}
		// System.out.println("orbitSession = " + orbitSession);
		return orbitSession;
	}

}
