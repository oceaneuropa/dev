package org.orbit.service.servlet;

import java.util.Dictionary;

import javax.servlet.Servlet;

import org.osgi.service.http.HttpContext;

public interface ServletMetadata {

	String getPath();

	Servlet getServlet();

	Dictionary<?, ?> getProperties();

	HttpContext getHttpContext(HttpContext defaultHttpContext);

}
