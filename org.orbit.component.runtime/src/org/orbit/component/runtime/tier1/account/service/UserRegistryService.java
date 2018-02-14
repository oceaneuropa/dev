package org.orbit.component.runtime.tier1.account.service;

import java.util.List;

import org.orbit.component.model.tier1.account.rto.UserAccount;
import org.orbit.component.model.tier1.account.rto.UserRegistryException;
import org.origin.common.rest.server.WebServiceAware;

public interface UserRegistryService extends WebServiceAware {

	public String getName();

	List<UserAccount> getUserAccounts() throws UserRegistryException;

	UserAccount getUserAccount(String userId) throws UserRegistryException;

	boolean userAccountExists(String userId) throws UserRegistryException;

	boolean matchUsernamePassword(String userId, String password) throws UserRegistryException;

	UserAccount registerUserAccount(UserAccount newUserAccountRequest) throws UserRegistryException;

	boolean updateUserAccount(UserAccount updateUserAccountRequest) throws UserRegistryException;

	boolean changePassword(String userId, String oldPassword, String newPassword) throws UserRegistryException;

	boolean isUserAccountActivated(String userId) throws UserRegistryException;

	boolean activateUserAccount(String userId) throws UserRegistryException;

	boolean deactivateUserAccount(String userId) throws UserRegistryException;

	boolean deleteUserAccount(String userId) throws UserRegistryException;

}
