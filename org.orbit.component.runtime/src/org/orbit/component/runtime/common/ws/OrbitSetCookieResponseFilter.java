package org.orbit.component.runtime.common.ws;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.NewCookie;

public class OrbitSetCookieResponseFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		NewCookie newCookie = new NewCookie("OrbitSession", "Einstein@mtswz");
		responseContext.getCookies().put("OrbitSession", newCookie);

		// responseContext.getHeaders().add("Set-Cookie", "OrbitSession=Einstein@mtswz");
	}

}
