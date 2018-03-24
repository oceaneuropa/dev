package org.origin.common.rest.filter;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

public class TestRequestFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String method = requestContext.getMethod();
		Request request = requestContext.getRequest();
		UriInfo uriInfo = requestContext.getUriInfo();
		MultivaluedMap<String, String> requestHeaders = requestContext.getHeaders();
		Map<String, Cookie> requestCookies = requestContext.getCookies();

		System.out.println(getClass().getSimpleName() + ".filter()");
		System.out.println("\tmethod = " + method);

		System.out.println("\trequest = " + request);
		String reqMethod = request.getMethod();
		System.out.println("\t\treqMethod = " + reqMethod);

		System.out.println("\turiInfo = " + uriInfo);
		URI absPath = uriInfo.getAbsolutePath();
		URI baseUri = uriInfo.getBaseUri();
		String path = uriInfo.getPath();
		URI reqUri = uriInfo.getRequestUri();
		System.out.println("\t\tabsPath = " + absPath);
		System.out.println("\t\tbaseUri = " + baseUri);
		System.out.println("\t\treqUri = " + reqUri);
		System.out.println("\t\tpath = " + path);

		System.out.println("\trequest headers = ");
		if (requestHeaders != null) {
			for (Iterator<String> itor = requestHeaders.keySet().iterator(); itor.hasNext();) {
				String headerName = itor.next();
				String headerValueString = requestContext.getHeaderString(headerName);
				System.out.println("\t\t" + headerName + " = " + headerValueString);
			}
		}

		System.out.println("\trequest cookies = ");
		if (requestCookies != null) {
			for (Iterator<String> itor = requestCookies.keySet().iterator(); itor.hasNext();) {
				String cookieName = itor.next();
				Cookie cookie = requestCookies.get(cookieName);
				System.out.println("\t\t" + cookieName + " = " + cookie.toString());
			}
		}
	}

}
