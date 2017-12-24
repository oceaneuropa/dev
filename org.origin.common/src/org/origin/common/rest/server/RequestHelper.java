package org.origin.common.rest.server;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

public class RequestHelper {

	public static RequestHelper INSTANCE = new RequestHelper();

	public String getPathParam(String paramName, ContainerRequestContext requestContext) {
		return requestContext.getUriInfo().getPathParameters().getFirst(paramName);
	}

	public List<String> getPathParams(String paramName, ContainerRequestContext requestContext) {
		return requestContext.getUriInfo().getPathParameters().get(paramName);
	}

	public String getQueryParam(String paramName, ContainerRequestContext requestContext) {
		return requestContext.getUriInfo().getQueryParameters().getFirst(paramName);
	}

	public List<String> getQueryParams(String paramName, ContainerRequestContext requestContext) {
		return requestContext.getUriInfo().getQueryParameters().get(paramName);
	}

	public boolean getHasQueryParam(String paramName, ContainerRequestContext requestContext) {
		return requestContext.getUriInfo().getQueryParameters().containsKey(paramName);
	}

}
