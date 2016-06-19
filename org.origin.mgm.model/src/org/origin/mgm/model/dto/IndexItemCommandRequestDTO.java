package org.origin.mgm.model.dto;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import org.origin.common.json.JSONUtil;

public class IndexItemCommandRequestDTO {
	@XmlElement
	protected String command;
	@XmlElement
	protected Map<String, Object> parameters;

	@XmlElement
	public String getCommand() {
		return this.command;
	}

	public void setCommand(String action) {
		this.command = action;
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
		sb.append("IndexItemCommandRequestDTO (");
		sb.append("command").append(this.command);
		sb.append(", parameters=").append(parametersString);
		sb.append(")");
		return sb.toString();
	}

}
