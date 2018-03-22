package org.origin.common.rest.util;

public interface WebServiceAware {

	// OSGi service property names
	public static final String PROP_NAME = "name";
	public static final String PROP_HOST_URL = "host_url";
	public static final String PROP_CONTEXT_ROOT = "context_root";
	public static WebServiceAware[] EMPTY_ARRAY = new WebServiceAware[] {};

	String getName();

	String getHostURL();

	String getContextRoot();

}
