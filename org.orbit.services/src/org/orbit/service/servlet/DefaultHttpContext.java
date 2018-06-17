package org.orbit.service.servlet;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.http.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultHttpContext implements HttpContext {

	private static final Logger logger = LoggerFactory.getLogger(DefaultHttpContext.class);

	private final HttpContext httpContext;

	public DefaultHttpContext(HttpContext httpContext) {
		this.httpContext = httpContext;
	}

	@Override
	public String getMimeType(String name) {
		return this.httpContext.getMimeType(name);
	}

	@Override
	public URL getResource(String name) {
		URL url = this.httpContext.getResource(name);
		logger.debug("Requested <{}>, resource is {}", name, url);
		return url;
	}

	@Override
	public boolean handleSecurity(HttpServletRequest arg0, HttpServletResponse arg1) throws IOException {
		return true;
	}

}
