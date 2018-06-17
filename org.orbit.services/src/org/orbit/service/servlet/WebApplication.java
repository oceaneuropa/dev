package org.orbit.service.servlet;

import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;

public interface WebApplication {

	String getContextRoot();

	ResourceMetadata[] getResources();

	ServletMetadata[] getServlets();

	JspMetadata[] getJSPs();

	HttpContext getHttpContext(HttpService httpService);

}
