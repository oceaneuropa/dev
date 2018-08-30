package org.orbit.component.connector.tier1.account;

import java.text.DateFormat;
import java.util.Date;

import org.orbit.component.api.tier1.account.UserAccount;
import org.origin.common.util.DateUtil;

public class UserAccountImpl implements UserAccount {

	private String accountId; // required
	private String username; // required
	private String password; // required
	private String email; // required
	private String firstName;
	private String lastName;
	private String phone;
	private Date creationTime;
	private Date lastUpdateTime;
	private boolean activated;

	public UserAccountImpl() {
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
	 * @param creationTime
	 * @param lastUpdateTime
	 */
	public UserAccountImpl(String accountId, String username, String password, String email, String firstName, String lastName, String phone, Date creationTime, Date lastUpdateTime) {
		this.accountId = accountId;
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.creationTime = creationTime;
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public Date getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public boolean isActivated() {
		return this.activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	protected DateFormat getDateFormat() {
		return DateUtil.getJdbcDateFormat();
	}

	@Override
	public String toString() {
		String creationTimeString = creationTime != null ? DateUtil.toString(creationTime, getDateFormat()) : null;
		String lastUpdateTimeString = lastUpdateTime != null ? DateUtil.toString(lastUpdateTime, getDateFormat()) : null;

		StringBuilder sb = new StringBuilder();
		sb.append("UserAccountImpl(");
		sb.append("accountId=").append(this.accountId);
		sb.append(", username=").append(this.username);
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
