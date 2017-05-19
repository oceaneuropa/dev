package org.orbit.component.connector.tier1.account;

import java.text.DateFormat;
import java.util.Date;

import org.orbit.component.api.tier1.account.UserAccount;
import org.origin.common.util.DateUtil;

public class UserAccountImpl implements UserAccount {

	private static boolean debug = true;

	private String userId; // required
	private String password; // required
	private String email; // required
	private String firstName;
	private String lastName;
	private String phone;
	private Date creationTime;
	private Date lastUpdateTime;

	public UserAccountImpl() {
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
	 */
	UserAccountImpl(String userId, String password, String email, String firstName, String lastName, String phone, Date creationTime, Date lastUpdateTime) {
		this.userId = userId;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.creationTime = creationTime;
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public String getId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	// public String getPassword() {
	// return password;
	// }
	//
	// public void setPassword(String password) {
	// this.password = password;
	// }

	@Override
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
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
		sb.append("userID=").append(this.userId);
		if (debug) {
			sb.append(", password=").append(this.password);
		}
		sb.append(", email=").append(this.email);
		sb.append(", firstName=").append(this.firstName);
		sb.append(", lastName=").append(this.lastName);
		sb.append(", phone=").append(this.phone);
		sb.append(", creationTime=").append(creationTimeString);
		sb.append(", lastUpdateTime=").append(lastUpdateTimeString);
		sb.append(")");
		return sb.toString();
	}

}
