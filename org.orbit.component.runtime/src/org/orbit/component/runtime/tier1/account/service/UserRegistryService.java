package org.orbit.component.runtime.tier1.account.service;

import java.util.List;

import org.orbit.component.model.tier1.account.UserAccountRTO;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.WebServiceAware;

public interface UserRegistryService extends WebServiceAware {

	public String getName();

	List<UserAccountRTO> getUserAccounts() throws ServerException;

	UserAccountRTO getUserAccount(String userId) throws ServerException;

	boolean userAccountExists(String userId) throws ServerException;

	boolean matchUsernamePassword(String userId, String password) throws ServerException;

	UserAccountRTO registerUserAccount(UserAccountRTO newUserAccountRequest) throws ServerException;

	boolean updateUserAccount(UserAccountRTO updateUserAccountRequest) throws ServerException;

	boolean changePassword(String userId, String oldPassword, String newPassword) throws ServerException;

	boolean isUserAccountActivated(String userId) throws ServerException;

	boolean activateUserAccount(String userId) throws ServerException;

	boolean deactivateUserAccount(String userId) throws ServerException;

	boolean deleteUserAccount(String userId) throws ServerException;

}
