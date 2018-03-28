package org.orbit.component.runtime.common.ws.other;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.origin.common.rest.model.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PreMatching
@Priority(value = 3)
@Provider
public class AccessTokenRequestFilterV1 implements ContainerRequestFilter {

	protected static Logger LOG = LoggerFactory.getLogger(AccessTokenRequestFilterV1.class);

	public AccessTokenRequestFilterV1() {
		LOG.info("CommonRequestFilter() initialization");
	}

	@Override
	public void filter(ContainerRequestContext requestContext) {
		LOG.info("filter()");

		MultivaluedMap<String, String> requestHeaders = requestContext.getHeaders();
		Map<String, Cookie> cookies = requestContext.getCookies();

		System.out.println("Headers:");
		System.out.println("-----------------------------------------------------------------");
		for (Iterator<String> headerNameItor = requestHeaders.keySet().iterator(); headerNameItor.hasNext();) {
			String headerName = headerNameItor.next();
			String firstHeaderValue = requestHeaders.getFirst(headerName);
			List<String> headerValues = requestHeaders.get(headerName);
			String headerValuesString = Arrays.toString(headerValues.toArray(new String[headerValues.size()]));
			System.out.println(headerName + " = " + firstHeaderValue + ".. " + headerValuesString);
		}
		System.out.println("-----------------------------------------------------------------");

		System.out.println("Cookies:");
		System.out.println("-----------------------------------------------------------------");
		int i = 0;
		for (Iterator<String> cookieItor = cookies.keySet().iterator(); cookieItor.hasNext();) {
			String cookieName = cookieItor.next();
			Cookie cookie = cookies.get(cookieName);

			int version = cookie.getVersion();
			String domain = cookie.getDomain();
			String name = cookie.getName();
			String path = cookie.getPath();
			String value = cookie.getValue();

			System.out.println("Cookie[" + i + "] (version=" + version + ", domain=" + domain + ", name=" + name + ", path=" + path + ", value=" + value + ")");
			i++;
		}
		System.out.println("-----------------------------------------------------------------");

		boolean isAuthorized = true;
		if (!isAuthorized) {
			Response response = Response.status(Status.UNAUTHORIZED).entity(new ErrorDTO("401", "Unauthorized", "Not authorized.")).build();
			requestContext.abortWith(response);
		}

		// String userName = requestContext.getUriInfo().getQueryParameters().getFirst("UserName");
		// if (userName == null || "".equals(userName)) {
		// LOG.info("Authentication Filter Failed");
		//
		// ResponseBuilder responseBuilder = Response.serverError();
		// Response response = responseBuilder.status(Status.BAD_REQUEST).build();
		// requestContext.abortWith(response);
		//
		// } else {
		// LOG.info("Authentication Filter Passed; UserName is " + userName);
		// }
	}

}
