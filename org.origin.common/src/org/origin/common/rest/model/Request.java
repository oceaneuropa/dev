package org.origin.common.rest.model;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Request {

	@XmlElement
	protected String requestName;
	@XmlElement
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

	@XmlElement
	public String getRequestName() {
		return this.requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	@XmlElement
	public Map<String, Object> getParameters() {
		return this.parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	@XmlElement
	public Object getParameter(String paramName) {
		return this.parameters.get(paramName);
	}

	public void setParameter(String paramName, Object paramValue) {
		if (paramValue == null) {
			this.parameters.remove(paramName);
		} else {
			this.parameters.put(paramName, paramValue);
		}
	}

}
