package org.orbit.component.api.tier1.account;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface UserAccountClient extends ServiceClient {

	UserAccount[] getUserAccounts() throws ClientException;

	UserAccount getUserAccount(String accountId) throws ClientException;

	boolean usernameExists(String username) throws ClientException;

	boolean emailExists(String email) throws ClientException;

	boolean register(CreateUserAccountRequest request) throws ClientException;

	boolean update(UpdateUserAccountRequest request) throws ClientException;

	boolean changePassword(String accountId, String oldPassword, String newPassword) throws ClientException;

	boolean isActivated(String accountId) throws ClientException;

	boolean activate(String accountId) throws ClientException;

	boolean deactivate(String accountId) throws ClientException;

	boolean delete(String accountId) throws ClientException;

}
