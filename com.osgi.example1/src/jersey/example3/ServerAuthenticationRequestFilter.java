package jersey.example3;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

/**
 * http://javatech-blog.blogspot.com/2015/04/jax-rs-filters-example.html
 *
 */
@PreMatching
@Priority(value = 3)
@Provider
public class ServerAuthenticationRequestFilter implements ContainerRequestFilter {

	public ServerAuthenticationRequestFilter() {
		System.out.println("ServerAuthenticationRequestFilter initialization");
	}

	@Override
	public void filter(ContainerRequestContext requestContext) {
		ResponseBuilder responseBuilder = null;
		Response response = null;
		String userName = null;
		System.out.println("filter() on ServerAuthenticationRequestFilter");

		userName = requestContext.getUriInfo().getQueryParameters().getFirst("UserName");
		if (userName == null || "".equals(userName)) {
			System.out.println("Authentication Filter Failed");
			responseBuilder = Response.serverError();
			response = responseBuilder.status(Status.BAD_REQUEST).build();
			requestContext.abortWith(response);

		} else {
			System.out.println("Authentication Filter Passed; UserName is " + userName);
		}
	}

}
