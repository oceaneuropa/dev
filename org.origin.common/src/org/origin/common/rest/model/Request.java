package org.origin.common.rest.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Request {

	protected String requestName;
	protected Map<String, Object> parameters = new LinkedHashMap<String, Object>();

	public Request() {
	}

	public Request(String requestName) {
		this.requestName = requestName;
	}

	public Request(String requestName, Map<String, Object> parameters) {
		this.requestName = requestName;
		this.parameters = parameters;
	}

	public String getRequestName() {
		return this.requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public Map<String, Object> getParameters() {
		return this.parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Object getParameter(String paramName) {
		return this.parameters.get(paramName);
	}

	public void setParameter(String paramName, Object paramValue) {
		this.parameters.put(paramName, paramValue);
	}

	public void removeParameter(String paramName) {
		this.parameters.remove(paramName);
	}

}
