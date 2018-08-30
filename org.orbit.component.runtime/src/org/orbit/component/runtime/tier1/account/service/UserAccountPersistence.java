package org.orbit.component.runtime.tier1.account.service;

import java.io.IOException;
import java.util.List;

import org.orbit.component.runtime.model.account.UserAccount;

public interface UserAccountPersistence {

	List<UserAccount> getUserAccounts() throws IOException;

	boolean accountIdExists(String accountId) throws IOException;

	boolean usernameExists(String username) throws IOException;

	boolean emailExists(String email) throws IOException;

	UserAccount getUserAccountByAccountId(String accountId) throws IOException;

	UserAccount getUserAccountByUsername(String username) throws IOException;

	UserAccount getUserAccountByEmail(String email) throws IOException;

	UserAccount createUserAccount(String accountId, String username, String password, String email, String firstName, String lastName, String phone) throws IOException;

	boolean setActivated(String accountId, boolean activated) throws IOException;

	boolean updatePassword(String accountId, String password) throws IOException;

	boolean updateName(String accountId, String firstName, String lastName) throws IOException;

	boolean updateEmail(String accountId, String email) throws IOException;

	boolean updatePhone(String accountId, String phone) throws IOException;

	boolean deleteUserAccount(String accountId) throws IOException;

}
