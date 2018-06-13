package org.orbit.service.servlet;

import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;

public interface WebApplication {

	String getContextRoot();

	ServletMetadata[] getServlets();

	ResourceMetadata[] getResources();

	HttpContext getHttpContext(HttpService httpService);

}
