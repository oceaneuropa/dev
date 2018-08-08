package co.edu.uniandes.nosqljpa.auth;

import static co.edu.uniandes.nosqljpa.auth.AuthenticationFilter.AUTHENTICATION_SCHEME;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.auth0.jwt.JWT;

@Secured
@Provider
// @Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

	public enum Role {
		admin, user, service
	}

	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Get the resource class which matches with the requested URL
		// Extract the roles declared by it
		Class<?> resourceClass = resourceInfo.getResourceClass();
		List<Role> classRoles = extractRoles(resourceClass);

		// Get the resource method which matches with the requested URL
		// Extract the roles declared by it
		Method resourceMethod = resourceInfo.getResourceMethod();
		List<Role> methodRoles = extractRoles(resourceMethod);

		try {

			// Check if the user is allowed to execute the method
			// The method annotations override the class annotations
			if (methodRoles.isEmpty()) {
				checkPermissions(requestContext, classRoles);
			} else {
				checkPermissions(requestContext, methodRoles);
			}

		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).build());
		}
	}

	// Extract the roles from the annotated element
	private List<Role> extractRoles(AnnotatedElement annotatedElement) {
		if (annotatedElement == null) {
			return new ArrayList<>();
		} else {
			Secured secured = annotatedElement.getAnnotation(Secured.class);
			if (secured == null) {
				return new ArrayList<>();
			} else {
				Role[] allowedRoles = secured.value();
				return Arrays.asList(allowedRoles);
			}
		}
	}

	private void checkPermissions(ContainerRequestContext requestContext, List<Role> allowedRoles) throws Exception {
		// Check if the user contains one of the allowed roles
		// Throw an Exception if the user has not permission to execute the method
		if (allowedRoles.isEmpty())
			return;
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
		List<String> roles = new ArrayList<String>();
		if (!JWT.decode(token).getClaim("gty").isNull() && JWT.decode(token).getClaim("gty").asString().equals("client-credentials")) {
			roles.add("service");
		} else {
			roles = JWT.decode(token).getClaim("roles").asList(String.class);
		}
		for (String role : roles) {
			if (allowedRoles.contains(Role.valueOf(role)))
				return;
		}
		throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).build());
	}

}