package org.origin.common.rest.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

/**
 * 
 * @see https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-2rd-edition/content/en/part1/chapter12/server_side_filters.html
 * 
 *      Prematching ContainerRequestFilters are designated with the @PreMatching annotation and will execute before the JAX-RS resource method is matched with
 *      the incoming HTTP request. Prematching filters often are used to modify request attributes to change how they match to a specific resource. For example,
 *      some firewalls do not allow PUT and/or DELETE invocations. To circumvent this limitation, many applications tunnel the HTTP method through the HTTP
 *      header X-Http-Method-Override
 * 
 *      This HttpMethodOverride filter will run before the HTTP request is matched to a specific JAX-RS method. The ContainerRequestContext parameter passed to
 *      the filter() method provides information about the request like headers, the URI, and so on. The filter() method uses the ContainerRequestContext
 *      parameter to check the value of the X-Http-Method-Override header. If the header is set in the request, the filter overrides the request’s HTTP method
 *      by calling ContainerRequestFilter.setMethod(). Filters can modify pretty much anything about the incoming request through methods on
 *      ContainerRequestContext, but once the request is matched to a JAX-RS method, a filter cannot modify the request URI or HTTP method.
 * 
 */
@Provider
@PreMatching
public class HttpMethodOverride implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext ctx) throws IOException {
		String methodOverride = ctx.getHeaderString("X-Http-Method-Override");
		if (methodOverride != null)
			ctx.setMethod(methodOverride);
	}

}
