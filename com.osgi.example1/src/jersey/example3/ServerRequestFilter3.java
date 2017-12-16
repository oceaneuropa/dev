package jersey.example3;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(value = 2)
public class ServerRequestFilter3 implements ContainerRequestFilter {

	public ServerRequestFilter3() {
		System.out.println("ServerRequestFilter3 initialization");
	}

	@Override
	public void filter(ContainerRequestContext requestContext) {
		System.out.println("filter() on ServerRequestFilter3");
	}

}
