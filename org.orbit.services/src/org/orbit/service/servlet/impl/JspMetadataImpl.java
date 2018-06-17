package org.orbit.service.servlet.impl;

import java.util.Dictionary;

import org.orbit.service.servlet.JspMetadata;
import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;

public class JspMetadataImpl implements JspMetadata {

	protected String path;
	protected Bundle bundle;
	protected String bundleResourcePath;
	protected Dictionary<?, ?> properties;
	protected HttpContext httpContext;

	public JspMetadataImpl() {
	}

	public JspMetadataImpl(Bundle bundle, String path, String bundleResourcePath, Dictionary<?, ?> properties) {
		this.bundle = bundle;
		this.path = path;
		this.bundleResourcePath = bundleResourcePath;
		this.properties = properties;
		// this.contextRoot = alias;
		// this.filePattern = filePattern;
	}

	public JspMetadataImpl(Bundle bundle, String path, String bundleResourcePath, Dictionary<?, ?> properties, HttpContext httpContext) {
		this.bundle = bundle;
		this.path = path;
		this.bundleResourcePath = bundleResourcePath;
		this.properties = properties;
		this.httpContext = httpContext;
		// this.contextRoot = contextRoot;
		// this.filePattern = filePattern;
	}

	@Override
	public Bundle getBundle() {
		return this.bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

	@Override
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String getBundleResourcePath() {
		return this.bundleResourcePath;
	}

	public void setBundleResourcePath(String bundleResourcePath) {
		this.bundleResourcePath = bundleResourcePath;
	}

	@Override
	public Dictionary<?, ?> getProperties() {
		return this.properties;
	}

	public void setProperties(Dictionary<?, ?> properties) {
		this.properties = properties;
	}

	@Override
	public HttpContext getHttpContext(HttpContext defaultHttpContext) {
		if (this.httpContext == null && defaultHttpContext != null) {
			return defaultHttpContext;
		}
		return this.httpContext;
	}

	public void setHttpContext(HttpContext httpContext) {
		this.httpContext = httpContext;
	}

}

// protected String contextRoot;
// protected String[] filePattern = new String[] { "*.jsp" };

// @Override
// public String getContextRoot() {
// return this.contextRoot;
// }
//
// public void setContextRoot(String contextRoot) {
// this.contextRoot = contextRoot;
// }

// @Override
// public String[] getFilePattern() {
// return this.filePattern;
// }
//
// public void setFilePattern(String[] filePattern) {
// this.filePattern = filePattern;
// }
