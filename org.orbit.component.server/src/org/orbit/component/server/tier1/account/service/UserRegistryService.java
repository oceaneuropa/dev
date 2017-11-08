package org.orbit.component.server.tier1.account.service;

import java.util.List;

import org.orbit.component.model.tier1.account.UserAccount;
import org.orbit.component.model.tier1.account.UserRegistryException;

public interface UserRegistryService {

	public String getNamespace();

	public String getName();

	public String getHostURL();

	public String getContextRoot();

	List<UserAccount> getUserAccounts() throws UserRegistryException;

	UserAccount getUserAccount(String userId) throws UserRegistryException;

	boolean userAccountExists(String userId) throws UserRegistryException;

	UserAccount registerUserAccount(UserAccount newUserAccountRequest) throws UserRegistryException;

	boolean updateUserAccount(UserAccount updateUserAccountRequest) throws UserRegistryException;

	boolean changePassword(String userId, String oldPassword, String newPassword) throws UserRegistryException;

	boolean isUserAccountActivated(String userId) throws UserRegistryException;

	boolean activateUserAccount(String userId) throws UserRegistryException;

	boolean deactivateUserAccount(String userId) throws UserRegistryException;

	boolean deleteUserAccount(String userId) throws UserRegistryException;

}
