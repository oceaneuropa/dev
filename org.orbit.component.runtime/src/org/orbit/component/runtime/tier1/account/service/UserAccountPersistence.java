package org.orbit.component.runtime.tier1.account.service;

import java.io.IOException;
import java.util.List;

import org.orbit.component.model.tier1.account.UserAccount;

public interface UserAccountPersistence {

	List<UserAccount> getUserAccounts() throws IOException;

	UserAccount getUserAccount(String userId) throws IOException;

	UserAccount createUserAccount(String userId, String password, String email, String firstName, String lastName, String phone) throws IOException;

	boolean setUserAccountActivated(String userId, boolean activated) throws IOException;

	boolean userAccountExists(String userId) throws IOException;

	boolean updatePassword(String userId, String password) throws IOException;

	boolean updateName(String userId, String firstName, String lastName) throws IOException;

	boolean updateEmail(String userId, String email) throws IOException;

	boolean updatePhone(String userId, String phone) throws IOException;

	boolean deleteUserAccount(String userId) throws IOException;

}
