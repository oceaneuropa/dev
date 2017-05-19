package org.orbit.component.api.tier1.account;

import java.util.Date;

public interface UserAccount {

	/**
	 * Get user id.
	 * 
	 * @return
	 */
	String getId();

	/**
	 * Get user email.
	 * 
	 * @return
	 */
	String getEmail();

	/**
	 * Get user first name.
	 * 
	 * @return
	 */
	String getFirstName();

	/**
	 * Get user last name.
	 * 
	 * @return
	 */
	String getLastName();

	/**
	 * Get user phone.
	 * 
	 * @return
	 */
	String getPhone();

	/**
	 * Get user account creation time.
	 * 
	 * @return
	 */
	Date getCreationTime();

	/**
	 * Get user account last update time.
	 * 
	 * @return
	 */
	Date getLastUpdateTime();

}
