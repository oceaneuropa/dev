package org.origin.common.rest.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Request {

	protected String label;
	protected Map<String, Object> parameters = new LinkedHashMap<String, Object>();

	public Request() {
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Map<String, Object> getParameters() {
		return parameters;
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
