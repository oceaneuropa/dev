package org.origin.common.rest.server;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

public class WSMethodDesc {

	// HTTP methods constants
	public static final String GET = HttpMethod.GET;
	public static final String PUT = HttpMethod.PUT;
	public static final String POST = HttpMethod.POST;
	public static final String DELETE = HttpMethod.DELETE;
	public static final String PATCH = "PATCH";

	// Produce constants
	public static final String JSON = MediaType.APPLICATION_JSON;

	protected WSResourceDesc parent;
	protected String method;
	protected String produces;
	protected String path;

	public WSMethodDesc() {
	}

	public WSMethodDesc(String method, String produces, String path) {
		this.method = method;
		this.produces = produces;
		this.path = path;
	}

	public WSMethodDesc(WSResourceDesc parent, String method, String produces, String path) {
		this.method = method;
		this.produces = produces;
		this.path = path;

		parent.addMethod(this);
	}

	public WSResourceDesc getParent() {
		return this.parent;
	}

	public void setParent(WSResourceDesc parent) {
		this.parent = parent;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getProduces() {
		return produces;
	}

	public void setProduces(String produces) {
		this.produces = produces;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
