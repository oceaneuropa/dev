package org.orbit.service.servlet;

import java.util.Dictionary;

import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;

public interface JspMetadata {

	Bundle getBundle();

	String getPath();

	String getBundleResourcePath();

	Dictionary<?, ?> getProperties();

	HttpContext getHttpContext(HttpContext defaultHttpContext);

}

// String getContextRoot();
// String[] getFilePattern();