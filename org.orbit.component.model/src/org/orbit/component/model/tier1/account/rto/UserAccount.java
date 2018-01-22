package org.orbit.component.model.tier1.account.rto;

import java.text.DateFormat;
import java.util.Date;

import org.origin.common.util.DateUtil;

public class UserAccount {

	protected String userId; // required
	protected String password; // required
	protected String email; // required
	protected String firstName;
	protected String lastName;
	protected String phone;
	protected Date creationTime;
	protected Date lastUpdateTime;
	protected boolean activated;

	/**
	 * 
	 */
	public UserAccount() {
	}

	/**
	 * 
	 * @param userId
	 * @param password
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param phone
	 * @param creationTime
	 * @param lastUpdateTime
	 * @param activated
	 */
	public UserAccount(String userId, String password, String email, String firstName, String lastName, String phone, Date creationTime, Date lastUpdateTime, boolean activated) {
		this.userId = userId;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.creationTime = creationTime;
		this.lastUpdateTime = lastUpdateTime;
		this.activated = activated;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public boolean isActivated() {
		return activated;
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
		sb.append("UserAccountRTO(");
		sb.append("userID=").append(this.userId);
		sb.append(", password=").append(this.password);
		sb.append(", email=").append(this.email);
		sb.append(", firstName=").append(this.firstName);
		sb.append(", lastName=").append(this.lastName);
		sb.append(", phone=").append(this.phone);
		sb.append(", creationTime=").append(creationTimeString);
		sb.append(", lastUpdateTime=").append(lastUpdateTimeString);
		sb.append(", activated=").append(activated);
		sb.append(")");
		return sb.toString();
	}

}
