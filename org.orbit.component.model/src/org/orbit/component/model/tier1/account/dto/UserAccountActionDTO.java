package org.orbit.component.model.tier1.account.dto;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO for user account action
 *
 */
@XmlRootElement
public class UserAccountActionDTO {

	@XmlElement
	protected String userId;
	@XmlElement
	protected String action;
	@XmlElement
	protected Map<Object, Object> args = new HashMap<Object, Object>();

	@XmlElement
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@XmlElement
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@XmlElement
	public Map<Object, Object> getArgs() {
		return args;
	}

	public void setArgs(Map<Object, Object> args) {
		this.args = args;
	}

}
