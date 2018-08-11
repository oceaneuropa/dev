package org.orbit.component.api.tier1.account;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface UserAccountClient extends ServiceClient {

	UserAccount[] getUserAccounts() throws ClientException;

	UserAccount getUserAccount(String userId) throws ClientException;

	boolean exists(String userId) throws ClientException;

	boolean register(CreateUserAccountRequest createUserAccountRequest) throws ClientException;

	boolean update(UpdateUserAccountRequest updateUserAccountRequest) throws ClientException;

	boolean changePassword(String userId, String oldPassword, String newPassword) throws ClientException;

	boolean isActivated(String userId) throws ClientException;

	boolean activate(String userId) throws ClientException;

	boolean deactivate(String userId) throws ClientException;

	boolean delete(String userId) throws ClientException;

}
