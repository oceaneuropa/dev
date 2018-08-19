package org.orbit.component.api.tier1.account;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface UserAccountClient extends ServiceClient {

	UserAccount[] getUserAccounts() throws ClientException;

	UserAccount getUserAccount(String username) throws ClientException;

	boolean usernameExists(String username) throws ClientException;

	boolean emailExists(String email) throws ClientException;

	boolean register(CreateUserAccountRequest createUserAccountRequest) throws ClientException;

	boolean update(UpdateUserAccountRequest updateUserAccountRequest) throws ClientException;

	boolean changePassword(String username, String oldPassword, String newPassword) throws ClientException;

	boolean isActivated(String username) throws ClientException;

	boolean activate(String username) throws ClientException;

	boolean deactivate(String username) throws ClientException;

	boolean delete(String username) throws ClientException;

}
