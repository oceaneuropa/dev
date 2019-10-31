package org.orbit.component.api.util;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.tier1.account.CreateUserAccountRequest;
import org.orbit.component.api.tier1.account.UpdateUserAccountRequest;
import org.orbit.component.api.tier1.account.UserAccount;
import org.orbit.component.api.tier1.account.UserAccountClient;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClientConstants;

public class UserAccountUtil {

	protected static UserAccount[] EMPTY_USER_ACCOUNTS = new UserAccount[0];

	/**
	 * 
	 * @param userRegistryUrl
	 * @param accessToken
	 * @return
	 */
	public static UserAccountClient getClient(String userRegistryUrl, String accessToken) {
		UserAccountClient userRegistry = null;
		if (userRegistryUrl != null) {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(WSClientConstants.REALM, null);
			properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
			properties.put(WSClientConstants.URL, userRegistryUrl);
			userRegistry = ComponentClients.getInstance().getUserAccounts(properties);
		}
		return userRegistry;
	}

	/**
	 * 
	 * @param userRegistryUrl
	 * @param accessToken
	 * @return
	 * @throws ClientException
	 */
	public static UserAccount[] getUserAccounts(String userRegistryUrl, String accessToken) throws ClientException {
		UserAccount[] userAccounts = null;
		UserAccountClient userAccountClient = getClient(userRegistryUrl, accessToken);
		if (userAccountClient != null) {
			userAccounts = userAccountClient.getUserAccounts();
		}
		if (userAccounts == null) {
			userAccounts = EMPTY_USER_ACCOUNTS;
		}
		return userAccounts;
	}

	/**
	 * 
	 * @param userRegistryUrl
	 * @param accessToken
	 * @param accountId
	 * @return
	 * @throws ClientException
	 */
	public static UserAccount getUserAccount(String userRegistryUrl, String accessToken, String accountId) throws ClientException {
		UserAccount userAccount = null;
		UserAccountClient userAccountClient = getClient(userRegistryUrl, accessToken);
		if (userAccountClient != null) {
			userAccount = userAccountClient.getUserAccount(accountId);
		}
		return userAccount;
	}

	/**
	 * 
	 * @param userRegistryUrl
	 * @param accessToken
	 * @param username
	 * @return
	 * @throws ClientException
	 */
	public static boolean usernameExists(String userRegistryUrl, String accessToken, String username) throws ClientException {
		boolean exists = false;
		UserAccountClient userAccountClient = getClient(userRegistryUrl, accessToken);
		if (userAccountClient != null) {
			exists = userAccountClient.usernameExists(username);
		}
		return exists;
	}

	/**
	 * 
	 * @param userRegistryUrl
	 * @param accessToken
	 * @param email
	 * @return
	 * @throws ClientException
	 */
	public static boolean emailExists(String userRegistryUrl, String accessToken, String email) throws ClientException {
		boolean exists = false;
		UserAccountClient userAccountClient = getClient(userRegistryUrl, accessToken);
		if (userAccountClient != null) {
			exists = userAccountClient.emailExists(email);
		}
		return exists;
	}

	/**
	 * 
	 * @param userRegistryUrl
	 * @param accessToken
	 * @param username
	 * @param email
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param phone
	 * @return
	 * @throws ClientException
	 */
	public static boolean addUserAccount(String userRegistryUrl, String accessToken, String username, String email, String password, String firstName, String lastName, String phone) throws ClientException {
		boolean succeed = false;
		UserAccountClient userAccountClient = getClient(userRegistryUrl, accessToken);
		if (userAccountClient != null) {
			CreateUserAccountRequest request = new CreateUserAccountRequest();

			request.setUsername(username);
			request.setPassword(password);
			request.setFirstName(firstName);
			request.setLastName(lastName);
			request.setEmail(email);
			request.setPhone(phone);

			succeed = userAccountClient.register(request);
		}
		return succeed;
	}

	/**
	 * 
	 * @param userRegistryUrl
	 * @param accessToken
	 * @param accountId
	 * @param username
	 * @param email
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param phone
	 * @return
	 * @throws ClientException
	 */
	public static boolean updateUserAccount(String userRegistryUrl, String accessToken, String accountId, String username, String email, String password, String firstName, String lastName, String phone) throws ClientException {
		boolean succeed = false;
		UserAccountClient userAccountClient = getClient(userRegistryUrl, accessToken);
		if (userAccountClient != null) {
			UpdateUserAccountRequest request = new UpdateUserAccountRequest();

			request.setAccountId(accountId);
			request.setUsername(username);
			request.setPassword(password);
			request.setEmail(email);
			request.setFirstName(firstName);
			request.setLastName(lastName);
			request.setPhone(phone);

			succeed = userAccountClient.update(request);
		}
		return succeed;
	}

	/**
	 * 
	 * @param userRegistryUrl
	 * @param accessToken
	 * @param accountId
	 * @return
	 * @throws ClientException
	 */
	public static boolean deleteUserAccount(String userRegistryUrl, String accessToken, String accountId) throws ClientException {
		boolean exists = false;
		UserAccountClient userAccountClient = getClient(userRegistryUrl, accessToken);
		if (userAccountClient != null) {
			exists = userAccountClient.delete(accountId);
		}
		return exists;
	}

}
