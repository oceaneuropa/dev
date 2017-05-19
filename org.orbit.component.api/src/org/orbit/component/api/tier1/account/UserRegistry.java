package org.orbit.component.api.tier1.account;

import java.util.Map;

import org.orbit.component.api.tier1.account.request.CreateUserAccountRequest;
import org.orbit.component.api.tier1.account.request.UpdateUserAccountRequest;
import org.origin.common.rest.client.ClientException;

public interface UserRegistry {

	/**
	 * Get UserRegistry name.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Get UserRegistry URL.
	 * 
	 * @return
	 */
	String getURL();

	/**
	 * Get properties.
	 * 
	 * @return
	 */
	Map<String, Object> getProperties();

	/**
	 * Ping the service.
	 * 
	 * @return
	 */
	boolean ping();

	/**
	 * Update properties.
	 * 
	 * @param properties
	 */
	void update(Map<String, Object> properties);

	/**
	 * Get user accounts.
	 * 
	 * @return
	 * @throws ClientException
	 */
	UserAccount[] getUserAccounts() throws ClientException;

	/**
	 * Get a user account.
	 * 
	 * @param userId
	 * @return
	 * @throws ClientException
	 */
	UserAccount getUserAccount(String userId) throws ClientException;

	/**
	 * Check whether a user account exists.
	 * 
	 * @param userId
	 * @return
	 * @throws ClientException
	 */
	boolean exists(String userId) throws ClientException;

	/**
	 * Add a user account.
	 * 
	 * @param createUserAccountRequest
	 * @return
	 * @throws ClientException
	 */
	boolean register(CreateUserAccountRequest createUserAccountRequest) throws ClientException;

	/**
	 * Update a user account.
	 * 
	 * @param updateUserAccountRequest
	 * @return
	 * @throws ClientException
	 */
	boolean update(UpdateUserAccountRequest updateUserAccountRequest) throws ClientException;

	/**
	 * Delete a user account.
	 * 
	 * @param userId
	 * @throws ClientException
	 */
	boolean delete(String userId) throws ClientException;

	/**
	 * Check whether a user account is activated.
	 * 
	 * @param userId
	 * @return
	 * @throws ClientException
	 */
	boolean isActivated(String userId) throws ClientException;

}
