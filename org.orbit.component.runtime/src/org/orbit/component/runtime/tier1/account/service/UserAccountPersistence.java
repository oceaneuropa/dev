package org.orbit.component.runtime.tier1.account.service;

import java.io.IOException;
import java.util.List;

import org.orbit.component.model.tier1.account.UserAccountRTO;

public interface UserAccountPersistence {

	List<UserAccountRTO> getUserAccounts() throws IOException;

	UserAccountRTO getUserAccount(String userId) throws IOException;

	UserAccountRTO createUserAccount(String userId, String password, String email, String firstName, String lastName, String phone) throws IOException;

	boolean setUserAccountActivated(String userId, boolean activated) throws IOException;

	boolean userAccountExists(String userId) throws IOException;

	boolean updatePassword(String userId, String password) throws IOException;

	boolean updateName(String userId, String firstName, String lastName) throws IOException;

	boolean updateEmail(String userId, String email) throws IOException;

	boolean updatePhone(String userId, String phone) throws IOException;

	boolean deleteUserAccount(String userId) throws IOException;

}
