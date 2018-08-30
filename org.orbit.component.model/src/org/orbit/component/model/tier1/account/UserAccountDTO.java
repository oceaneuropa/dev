package org.orbit.component.model.tier1.account;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User account DTO
 *
 */
@XmlRootElement
public class UserAccountDTO {

	@XmlElement
	protected String accountId;
	@XmlElement
	protected String username;
	@XmlElement
	protected String password;
	@XmlElement
	protected String email;
	@XmlElement
	protected String firstName;
	@XmlElement
	protected String lastName;
	@XmlElement
	protected String phone;
	@XmlElement
	protected Date creationTime;
	@XmlElement
	protected Date lastUpdateTime;
	@XmlElement
	protected boolean activated;

	@XmlElement
	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@XmlElement
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@XmlElement
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlElement
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlElement
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@XmlElement
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@XmlElement
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@XmlElement
	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@XmlElement
	public Date getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@XmlElement
	public boolean isActivated() {
		return this.activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

}
