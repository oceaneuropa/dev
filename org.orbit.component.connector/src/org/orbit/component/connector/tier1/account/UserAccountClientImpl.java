package org.orbit.component.connector.tier1.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.account.CreateUserAccountRequest;
import org.orbit.component.api.tier1.account.UpdateUserAccountRequest;
import org.orbit.component.api.tier1.account.UserAccount;
import org.orbit.component.api.tier1.account.UserAccountClient;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.connector.util.ModelConverter;
import org.orbit.component.model.tier1.account.UserAccountDTO;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.model.StatusDTO;

public class UserAccountClientImpl extends ServiceClientImpl<UserAccountClient, UserAccountWSClient> implements UserAccountClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public UserAccountClientImpl(ServiceConnector<UserAccountClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected UserAccountWSClient createWSClient(Map<String, Object> properties) {
		String realm = (String) this.properties.get(OrbitConstants.REALM);
		String username = (String) this.properties.get(OrbitConstants.USERNAME);
		String fullUrl = (String) this.properties.get(OrbitConstants.URL);

		ClientConfiguration clientConfig = ClientConfiguration.create(realm, username, fullUrl);
		return new UserAccountWSClient(clientConfig);
	}

	@Override
	public UserAccount[] getUserAccounts() throws ClientException {
		List<UserAccount> userAccounts = new ArrayList<UserAccount>();
		try {
			List<UserAccountDTO> userAccountDTOs = getWSClient().getUserAccounts();
			for (UserAccountDTO userAccountDTO : userAccountDTOs) {
				userAccounts.add(ModelConverter.Account.toUserAccountImpl(userAccountDTO));
			}
		} catch (ClientException e) {
			throw e;
		}
		return userAccounts.toArray(new UserAccount[userAccounts.size()]);
	}

	@Override
	public UserAccount getUserAccount(String userId) throws ClientException {
		checkUserId(userId);
		UserAccount userAccount = null;
		try {
			UserAccountDTO userAccountDTO = getWSClient().getUserAccount(userId);
			if (userAccountDTO != null) {
				userAccount = ModelConverter.Account.toUserAccountImpl(userAccountDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return userAccount;
	}

	@Override
	public boolean exists(String userId) throws ClientException {
		checkUserId(userId);
		try {
			return getWSClient().userAccountExists(userId);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean register(CreateUserAccountRequest createUserAccountRequest) throws ClientException {
		String userId = createUserAccountRequest.getUserId();
		checkUserId(userId);
		try {
			UserAccountDTO createUserAccountRequestDTO = ModelConverter.Account.toDTO(createUserAccountRequest);
			StatusDTO status = getWSClient().registerUserAccount(createUserAccountRequestDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean update(UpdateUserAccountRequest updateUserAccountRequest) throws ClientException {
		String userId = updateUserAccountRequest.getUserId();
		checkUserId(userId);
		try {
			UserAccountDTO updateUserAccountRequestDTO = ModelConverter.Account.toDTO(updateUserAccountRequest);
			StatusDTO status = getWSClient().updateUserAccount(updateUserAccountRequestDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean changePassword(String userId, String oldPassword, String newPassword) throws ClientException {
		checkUserId(userId);
		try {
			return getWSClient().changePassword(userId, oldPassword, newPassword);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean isActivated(String userId) throws ClientException {
		checkUserId(userId);
		try {
			return getWSClient().isUserAccountActivated(userId);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean activate(String userId) throws ClientException {
		checkUserId(userId);
		try {
			boolean succeed = getWSClient().activateUserAccount(userId);
			return succeed;
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean deactivate(String userId) throws ClientException {
		checkUserId(userId);
		try {
			boolean succeed = getWSClient().deactivateUserAccount(userId);
			return succeed;
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean delete(String userId) throws ClientException {
		checkUserId(userId);
		try {
			StatusDTO status = getWSClient().deleteUserAccount(userId);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	/**
	 * 
	 * @param userId
	 * @throws ClientException
	 */
	protected void checkUserId(String userId) throws ClientException {
		if (userId == null || userId.isEmpty()) {
			throw new ClientException(400, "userId is empty.");
		}
	}

}

// /**
// * Generate a unique id, for load balancing purpose, based on indexing properties for a user registry.
// *
// * @param properties
// * @return
// */
// protected String getLoadBalanceId(Map<String, Object> properties) {
// String userRegistryName = (String) properties.get(OrbitConstants.USER_REGISTRY_NAME);
// String url = (String) properties.get(OrbitConstants.USER_REGISTRY_HOST_URL);
// String contextRoot = (String) properties.get(OrbitConstants.USER_REGISTRY_CONTEXT_ROOT);
// String key = url + "::" + contextRoot + "::" + userRegistryName;
// return key;
// }
