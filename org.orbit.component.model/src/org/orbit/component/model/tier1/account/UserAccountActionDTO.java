package org.orbit.component.model.tier1.account;

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
	protected String accountId;
	@XmlElement
	protected String action;
	@XmlElement
	protected Map<Object, Object> args = new HashMap<Object, Object>();

	@XmlElement
	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@XmlElement
	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@XmlElement
	public Map<Object, Object> getArgs() {
		return this.args;
	}

	public void setArgs(Map<Object, Object> args) {
		this.args = args;
	}

}
