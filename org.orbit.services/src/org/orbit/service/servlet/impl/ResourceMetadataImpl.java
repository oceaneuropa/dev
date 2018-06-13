package org.orbit.service.servlet.impl;

import org.orbit.service.servlet.ResourceMetadata;
import org.osgi.service.http.HttpContext;

public class ResourceMetadataImpl implements ResourceMetadata {

	protected String path;
	protected String localPath;
	protected HttpContext httpContext;

	public ResourceMetadataImpl() {
	}

	public ResourceMetadataImpl(String path, String localPath) {
		this.path = path;
		this.localPath = localPath;
	}

	public ResourceMetadataImpl(String path, String localPath, HttpContext httpContext) {
		this.path = path;
		this.localPath = localPath;
		this.httpContext = httpContext;
	}

	@Override
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String getLocalPath() {
		return this.localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
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
