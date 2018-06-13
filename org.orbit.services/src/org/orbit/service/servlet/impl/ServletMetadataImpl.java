package org.orbit.service.servlet.impl;

import java.util.Dictionary;

import javax.servlet.Servlet;

import org.orbit.service.servlet.ServletMetadata;
import org.osgi.service.http.HttpContext;

public class ServletMetadataImpl implements ServletMetadata {

	protected String path;
	protected Servlet servlet;
	protected Dictionary<?, ?> properties;
	protected HttpContext httpContext;

	public ServletMetadataImpl() {
	}

	/**
	 * 
	 * @param path
	 * @param servlet
	 * @param properties
	 */
	public ServletMetadataImpl(String path, Servlet servlet, Dictionary<?, ?> properties) {
		this.path = path;
		this.servlet = servlet;
		this.properties = properties;
	}

	/**
	 * 
	 * @param path
	 * @param servlet
	 * @param properties
	 * @param httpContext
	 */
	public ServletMetadataImpl(String path, Servlet servlet, Dictionary<?, ?> properties, HttpContext httpContext) {
		this.path = path;
		this.servlet = servlet;
		this.properties = properties;
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
	public Servlet getServlet() {
		return this.servlet;
	}

	public void setServlet(Servlet servlet) {
		this.servlet = servlet;
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
