package org.origin.mgm.model.dto;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import org.origin.common.json.JSONUtil;

public class IndexItemActionDTO {
	@XmlElement
	protected String action;
	@XmlElement
	protected Map<String, Object> parameters;

	@XmlElement
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@XmlElement
	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	@Override
	public String toString() {
		String parametersString = JSONUtil.toJsonString(this.parameters);

		StringBuilder sb = new StringBuilder();
		sb.append("IndexItemActionDTO (");
		sb.append("action").append(this.action);
		sb.append(", parameters=").append(parametersString);
		sb.append(")");
		return sb.toString();
	}

}
