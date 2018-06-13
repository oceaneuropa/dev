package org.orbit.service.servlet;

import org.osgi.service.http.HttpContext;

public interface ResourceMetadata {

	String getPath();

	String getLocalPath();

	HttpContext getHttpContext(HttpContext defaultHttpContext);

}
