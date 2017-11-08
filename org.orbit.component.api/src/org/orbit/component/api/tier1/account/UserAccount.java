package org.orbit.component.api.tier1.account;

import java.util.Date;

public interface UserAccount {

	String getUserId();

	String getEmail();

	String getPassword();

	String getFirstName();

	String getLastName();

	String getPhone();

	boolean isActivated();

	Date getCreationTime();

	Date getLastUpdateTime();

}
