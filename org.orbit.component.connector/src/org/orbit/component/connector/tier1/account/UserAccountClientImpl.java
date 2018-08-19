package org.orbit.component.connector.tier1.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.account.CreateUserAccountRequest;
import org.orbit.component.api.tier1.account.UpdateUserAccountRequest;
import org.orbit.component.api.tier1.account.UserAccount;
import org.orbit.component.api.tier1.account.UserAccountClient;
import org.orbit.component.connector.util.ModelConverter;
import org.orbit.component.model.tier1.account.UserAccountDTO;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
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
		WSClientConfiguration config = WSClientConfiguration.create(this.properties);
		return new UserAccountWSClient(config);
	}

	/**
	 * 
	 * @param username
	 * @throws ClientException
	 */
	protected void checkUsername(String username) throws ClientException {
		if (username == null || username.isEmpty()) {
			throw new ClientException(400, "username is empty.");
		}
	}

	/**
	 * 
	 * @param email
	 * @throws ClientException
	 */
	protected void checkEmail(String email) throws ClientException {
		if (email == null || email.isEmpty()) {
			throw new ClientException(400, "email is empty.");
		}
	}

	@Override
	public UserAccount[] getUserAccounts() throws ClientException {
		List<UserAccount> userAccounts = new ArrayList<UserAccount>();
		try {
			List<UserAccountDTO> userAccountDTOs = getWSClient().getList();
			for (UserAccountDTO userAccountDTO : userAccountDTOs) {
				userAccounts.add(ModelConverter.Account.toUserAccountImpl(userAccountDTO));
			}
		} catch (ClientException e) {
			throw e;
		}
		return userAccounts.toArray(new UserAccount[userAccounts.size()]);
	}

	@Override
	public UserAccount getUserAccount(String username) throws ClientException {
		checkUsername(username);
		UserAccount userAccount = null;
		try {
			UserAccountDTO userAccountDTO = getWSClient().get(username);
			if (userAccountDTO != null) {
				userAccount = ModelConverter.Account.toUserAccountImpl(userAccountDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return userAccount;
	}

	@Override
	public boolean usernameExists(String username) throws ClientException {
		checkUsername(username);
		boolean exists = getWSClient().exists("username", username);
		return exists;
	}

	@Override
	public boolean emailExists(String email) throws ClientException {
		checkEmail(email);
		boolean exists = getWSClient().exists("email", email);
		return exists;
	}

	@Override
	public boolean register(CreateUserAccountRequest createUserAccountRequest) throws ClientException {
		String userId = createUserAccountRequest.getUserId();
		checkUsername(userId);
		try {
			UserAccountDTO createUserAccountRequestDTO = ModelConverter.Account.toDTO(createUserAccountRequest);
			StatusDTO status = getWSClient().create(createUserAccountRequestDTO);
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
		checkUsername(userId);
		try {
			UserAccountDTO updateUserAccountRequestDTO = ModelConverter.Account.toDTO(updateUserAccountRequest);
			StatusDTO status = getWSClient().update(updateUserAccountRequestDTO);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean changePassword(String username, String oldPassword, String newPassword) throws ClientException {
		checkUsername(username);
		try {
			return getWSClient().changePassword(username, oldPassword, newPassword);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean isActivated(String username) throws ClientException {
		checkUsername(username);
		try {
			return getWSClient().isActivated(username);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean activate(String username) throws ClientException {
		checkUsername(username);
		try {
			boolean succeed = getWSClient().activate(username);
			return succeed;
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean deactivate(String username) throws ClientException {
		checkUsername(username);
		try {
			boolean succeed = getWSClient().deactivate(username);
			return succeed;
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean delete(String username) throws ClientException {
		checkUsername(username);
		try {
			StatusDTO status = getWSClient().delete(username);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
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
