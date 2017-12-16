package jersey.example3;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(value = 1)
public class ServerResponseFilter implements ContainerResponseFilter {

	public ServerResponseFilter() {
		System.out.println("ServerResponseFilter initialization");
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
		System.out.println("filter() on ServerResponseFilter");

		if ("/select".equals(requestContext.getUriInfo().getPath()) && responseContext.hasEntity()) {
			responseContext.setEntity(responseContext.getEntity().toString() + "\nResponse changed in Response Filter");
		}
	}

}
