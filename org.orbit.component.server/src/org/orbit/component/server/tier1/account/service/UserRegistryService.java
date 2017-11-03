package org.orbit.component.server.tier1.account.service;

import java.util.List;

import org.orbit.component.model.tier1.account.UserAccountRTO;
import org.orbit.component.model.tier1.account.UserRegistryException;

public interface UserRegistryService {

	public String getNamespace();

	public String getName();

	public String getHostURL();

	public String getContextRoot();

	/**
	 * Get user accounts.
	 * 
	 * @return
	 * @throws UserRegistryException
	 */
	List<UserAccountRTO> getUserAccounts() throws UserRegistryException;

	/**
	 * Get a user account.
	 * 
	 * @param userId
	 * @return
	 * @throws UserRegistryException
	 */
	UserAccountRTO getUserAccount(String userId) throws UserRegistryException;

	/**
	 * Check whether a user account exists.
	 * 
	 * @param userId
	 * @return
	 * @throws UserRegistryException
	 */
	boolean userAccountExists(String userId) throws UserRegistryException;

	/**
	 * Register a user account.
	 * 
	 * @param newUserAccountRequest
	 * @return
	 * @throws UserRegistryException
	 */
	UserAccountRTO registerUserAccount(UserAccountRTO newUserAccountRequest) throws UserRegistryException;

	/**
	 * Update a user account.
	 * 
	 * @param updateUserAccountRequest
	 * @return
	 * @throws UserRegistryException
	 */
	boolean updateUserAccount(UserAccountRTO updateUserAccountRequest) throws UserRegistryException;

	/**
	 * Delete a user account.
	 * 
	 * @param userId
	 * @return
	 * @throws UserRegistryException
	 */
	boolean deleteUserAccount(String userId) throws UserRegistryException;

	/**
	 * Check whether a user account is activated.
	 * 
	 * @param userId
	 * @return
	 * @throws UserRegistryException
	 */
	boolean isUserAccountActivated(String userId) throws UserRegistryException;

	/**
	 * Activate a user account.
	 * 
	 * @param userId
	 * @return
	 * @throws UserRegistryException
	 */
	boolean activateUserAccount(String userId) throws UserRegistryException;

	/**
	 * Deactivate a user account.
	 * 
	 * @param userId
	 * @return
	 * @throws UserRegistryException
	 */
	boolean deactivateUserAccount(String userId) throws UserRegistryException;

}
