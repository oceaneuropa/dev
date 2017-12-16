package org.origin.common.rest.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.ext.Provider;

/**
 * 
 * @see https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-2rd-edition/content/en/part1/chapter12/server_side_filters.html
 * 
 *      Generally, you use these types of filters to decorate the response by adding or modifying response headers. One example is if you wanted to set a
 *      default Cache-Control header for each response to a GET request. Here’s what it might look like:
 *
 *      The ContainerResponseFilter.filter() method has two parameters. The ContainerRequestContext parameter gives you access to information about the request.
 *      Here we’re checking to see if the request was a GET. The ContainerResponseContext parameter allows you to view, add, and modify the response before it
 *      is marshalled and sent back to the client. In the example, we use the ContainerResponseContext to set a Cache-Control response header.
 * 
 */
@Provider
public class CacheControlFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
		if (request.getMethod().equals("GET")) {
			CacheControl cc = new CacheControl();
			cc.setMaxAge(100);
			response.getHeaders().add("Cache-Control", cc);
		}
	}

}
