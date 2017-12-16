package org.origin.common.rest.filter;

import java.io.IOException;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

/**
 * @see https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-2rd-edition/content/en/part1/chapter12/server_side_filters.html
 * 
 *      Another great use case for request filters is implementing custom authentication protocols. For example, OAuth 2.0 has a token protocol that is
 *      transmitted through the Authorization HTTP header. Here’s what an implementation of that might look like:
 * 
 *      In this example, if there is no Authorization header or it is invalid, the request is aborted with a NotAuthorizedException. The client receives a 401
 *      response with a WWW-Authenticate header set to the value passed into the constructor of NotAuthorizedException. If you want to avoid exception mapping,
 *      then you can use the ContainerRequestContext.abortWith() method instead. Generally, however, I prefer to throw exceptions.
 * 
 * @see BearerTokenFilter from server_side_filters.html
 * 
 */
@Provider
@PreMatching
public class BearerTokenFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext ctx) throws IOException {
		String authHeader = ctx.getHeaderString(HttpHeaders.AUTHORIZATION);
		// if (authHeader == null) {
		// throw new NotAuthorizedException("Bearer");
		// }

		String token = parseToken(authHeader);
		if (!verifyToken(token)) {
			throw new NotAuthorizedException("Bearer error=\"invalid_token\"");
		}
	}

	protected String parseToken(String header) {
		return null;
	}

	protected boolean verifyToken(String token) {
		return false;
	}

}
