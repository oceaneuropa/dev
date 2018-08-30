package org.orbit.component.runtime.model.account;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import org.origin.common.util.DateUtil;

public class UserAccount {

	protected String accountId; // required
	protected String username; // required
	protected String password; // required
	protected String email; // required
	protected String firstName;
	protected String lastName;
	protected String phone;
	protected boolean activated;
	protected Map<String, Object> properties;
	protected Date creationTime;
	protected Date lastUpdateTime;

	public UserAccount() {
	}

	/**
	 * 
	 * @param accountId
	 * @param username
	 * @param password
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param phone
	 * @param activated
	 * @param properties
	 * @param creationTime
	 * @param lastUpdateTime
	 */
	public UserAccount(String accountId, String username, String password, String email, String firstName, String lastName, String phone, boolean activated, Map<String, Object> properties, Date creationTime, Date lastUpdateTime) {
		this.accountId = accountId;
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.activated = activated;
		this.properties = properties;
		this.creationTime = creationTime;
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isActivated() {
		return this.activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	protected DateFormat getDateFormat() {
		return DateUtil.getJdbcDateFormat();
	}

	@Override
	public String toString() {
		String creationTimeString = this.creationTime != null ? DateUtil.toString(this.creationTime, getDateFormat()) : null;
		String lastUpdateTimeString = this.lastUpdateTime != null ? DateUtil.toString(this.lastUpdateTime, getDateFormat()) : null;

		StringBuilder sb = new StringBuilder();
		sb.append("UserAccount(");
		sb.append("accountId=").append(this.accountId);
		sb.append(", username=").append(this.username);
		sb.append(", password=").append(this.password);
		sb.append(", email=").append(this.email);
		sb.append(", firstName=").append(this.firstName);
		sb.append(", lastName=").append(this.lastName);
		sb.append(", phone=").append(this.phone);
		sb.append(", activated=").append(this.activated);
		sb.append(", creationTime=").append(creationTimeString);
		sb.append(", lastUpdateTime=").append(lastUpdateTimeString);
		sb.append(")");
		return sb.toString();
	}

}
