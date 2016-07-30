package org.nb.mgm.model.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Action {

	protected String name;
	protected Map<String, Object> parameters = new HashMap<String, Object>();

	/**
	 * 
	 */
	public Action() {
	}

	/**
	 * 
	 * @param name
	 * @param parameters
	 */
	public Action(String name, Map<String, Object> parameters) {
		this.name = name;
		this.parameters = parameters;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
