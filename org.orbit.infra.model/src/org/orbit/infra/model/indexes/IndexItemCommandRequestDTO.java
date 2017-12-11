package org.orbit.infra.model.indexes;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import org.origin.common.json.JSONUtil;

public class IndexItemCommandRequestDTO {

	@XmlElement
	protected String command;
	@XmlElement
	protected Map<String, Object> params;

	@XmlElement
	public String getCommand() {
		return this.command;
	}

	public void setCommand(String action) {
		this.command = action;
	}

	@XmlElement
	public Map<String, Object> getParameters() {
		return params;
	}

	public void setParameters(Map<String, Object> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		String parametersString = JSONUtil.toJsonString(this.params);
		StringBuilder sb = new StringBuilder();
		sb.append("IndexItemCommandRequestDTO (");
		sb.append("command").append(this.command);
		sb.append(", parameters=").append(parametersString);
		sb.append(")");
		return sb.toString();
	}

}
