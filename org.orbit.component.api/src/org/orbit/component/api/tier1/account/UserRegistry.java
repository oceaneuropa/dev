package org.orbit.component.api.tier1.account;

import java.util.Map;

import org.orbit.component.api.tier1.account.request.CreateUserAccountRequest;
import org.orbit.component.api.tier1.account.request.UpdateUserAccountRequest;
import org.origin.common.rest.client.ClientException;

public interface UserRegistry {

	String getName();

	String getURL();

	Map<String, Object> getProperties();

	boolean ping();

	void update(Map<String, Object> properties);

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
