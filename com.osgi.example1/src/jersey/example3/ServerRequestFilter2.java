package jersey.example3;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(value = 1)
public class ServerRequestFilter2 implements ContainerRequestFilter {

	public ServerRequestFilter2() {
		System.out.println("ServerRequestFilter2 initialization");
	}

	@Override
	public void filter(ContainerRequestContext requestContext) {
		System.out.println("filter() on ServerRequestFilter2");
	}

}
