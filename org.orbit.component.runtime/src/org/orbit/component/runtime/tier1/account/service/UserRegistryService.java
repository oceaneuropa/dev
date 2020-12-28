package org.orbit.component.runtime.tier1.account.service;

import java.util.List;

import org.orbit.component.runtime.model.account.UserAccount;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.AccessTokenProvider;
import org.origin.common.service.IWebService;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface UserRegistryService extends IWebService, AccessTokenProvider {

	List<UserAccount> getUserAccounts() throws ServerException;

	UserAccount getUserAccountByAccountId(String accountId) throws ServerException;

	UserAccount getUserAccountByUsername(String username) throws ServerException;

	boolean accountIdExists(String accountId) throws ServerException;

	boolean usernameExists(String username) throws ServerException;

	boolean emailExists(String email) throws ServerException;

	boolean matchUsernamePassword(String username, String password) throws ServerException;

	UserAccount registerUserAccount(UserAccount newUserAccountRequest) throws ServerException;

	boolean updateUserAccount(UserAccount updateUserAccountRequest) throws ServerException;

	boolean changePassword(String accountId, String oldPassword, String newPassword) throws ServerException;

	boolean isUserAccountActivated(String accountId) throws ServerException;

	boolean activateUserAccount(String accountId) throws ServerException;

	boolean deactivateUserAccount(String accountId) throws ServerException;

	boolean deleteUserAccount(String accountId) throws ServerException;

}
