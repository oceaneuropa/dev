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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRequestFilter implements ContainerRequestFilter {

	protected static Logger LOG = LoggerFactory.getLogger(TestRequestFilter.class);

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String method = requestContext.getMethod();
		Request request = requestContext.getRequest();
		UriInfo uriInfo = requestContext.getUriInfo();
		MultivaluedMap<String, String> requestHeaders = requestContext.getHeaders();
		Map<String, Cookie> requestCookies = requestContext.getCookies();

		LOG.debug(getClass().getSimpleName() + ".filter()");
		LOG.debug("\tmethod = " + method);

		LOG.debug("\trequest = " + request);
		String reqMethod = request.getMethod();
		LOG.debug("\t\treqMethod = " + reqMethod);

		LOG.debug("\turiInfo = " + uriInfo);
		URI absPath = uriInfo.getAbsolutePath();
		URI baseUri = uriInfo.getBaseUri();
		String path = uriInfo.getPath();
		URI reqUri = uriInfo.getRequestUri();
		LOG.debug("\t\tabsPath = " + absPath);
		LOG.debug("\t\tbaseUri = " + baseUri);
		LOG.debug("\t\treqUri = " + reqUri);
		LOG.debug("\t\tpath = " + path);

		LOG.debug("\trequest headers = ");
		if (requestHeaders != null) {
			for (Iterator<String> itor = requestHeaders.keySet().iterator(); itor.hasNext();) {
				String headerName = itor.next();
				String headerValueString = requestContext.getHeaderString(headerName);
				LOG.debug("\t\t" + headerName + " = " + headerValueString);
			}
		}

		LOG.debug("\trequest cookies = ");
		if (requestCookies != null) {
			for (Iterator<String> itor = requestCookies.keySet().iterator(); itor.hasNext();) {
				String cookieName = itor.next();
				Cookie cookie = requestCookies.get(cookieName);
				LOG.debug("\t\t" + cookieName + " = " + cookie.toString());
			}
		}
	}

}
