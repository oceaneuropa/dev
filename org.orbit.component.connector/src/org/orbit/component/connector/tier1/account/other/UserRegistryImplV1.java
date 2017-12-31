package org.orbit.component.connector.tier1.account.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.account.UserAccount;
import org.orbit.component.api.tier1.account.UserRegistry;
import org.orbit.component.api.tier1.account.request.CreateUserAccountRequest;
import org.orbit.component.api.tier1.account.request.UpdateUserAccountRequest;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.connector.tier1.account.UserAccountImpl;
import org.orbit.component.connector.tier1.account.UserRegistryWSClient;
import org.orbit.component.model.tier1.account.dto.UserAccountDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.model.StatusDTO;

public class UserRegistryImplV1 implements UserRegistry {

	protected Map<String, Object> properties;
	protected UserRegistryWSClient client;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public UserRegistryImplV1(ServiceConnector<UserRegistry> connector, Map<String, Object> properties) {
		if (connector != null) {
			adapt(ServiceConnector.class, connector);
		}
		this.properties = checkProperties(properties);
		initClient();
	}

	protected Map<String, Object> checkProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	@Override
	public boolean close() throws ClientException {
		@SuppressWarnings("unchecked")
		ServiceConnector<UserRegistry> connector = getAdapter(ServiceConnector.class);
		if (connector != null) {
			return connector.close(this);
		}
		return false;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	@Override
	public void update(Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		initClient();
	}

	protected void initClient() {
		String realm = (String) this.properties.get(OrbitConstants.REALM);
		String username = (String) this.properties.get(OrbitConstants.USERNAME);
		String fullUrl = (String) this.properties.get(OrbitConstants.URL);

		ClientConfiguration clientConfig = ClientConfiguration.create(realm, username, fullUrl);
		this.client = new UserRegistryWSClient(clientConfig);
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(OrbitConstants.USER_REGISTRY_NAME);
		return name;
	}

	@Override
	public String getURL() {
		String fullUrl = (String) properties.get(OrbitConstants.URL);
		return fullUrl;
	}

	@Override
	public boolean ping() {
		return this.client.doPing();
	}

	@Override
	public UserAccount[] getUserAccounts() throws ClientException {
		List<UserAccount> userAccounts = new ArrayList<UserAccount>();
		try {
			List<UserAccountDTO> userAccountDTOs = this.client.getUserAccounts();
			for (UserAccountDTO userAccountDTO : userAccountDTOs) {
				userAccounts.add(toUserAccountImpl(userAccountDTO));
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
			UserAccountDTO userAccountDTO = this.client.getUserAccount(userId);
			if (userAccountDTO != null) {
				userAccount = toUserAccountImpl(userAccountDTO);
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
			return this.client.userAccountExists(userId);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean register(CreateUserAccountRequest createUserAccountRequest) throws ClientException {
		String userId = createUserAccountRequest.getUserId();
		checkUserId(userId);
		try {
			UserAccountDTO createUserAccountRequestDTO = toDTO(createUserAccountRequest);
			StatusDTO status = this.client.registerUserAccount(createUserAccountRequestDTO);
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
			UserAccountDTO updateUserAccountRequestDTO = toDTO(updateUserAccountRequest);
			StatusDTO status = this.client.updateUserAccount(updateUserAccountRequestDTO);
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
			return this.client.changePassword(userId, oldPassword, newPassword);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean isActivated(String userId) throws ClientException {
		checkUserId(userId);
		try {
			return this.client.isUserAccountActivated(userId);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean activate(String userId) throws ClientException {
		checkUserId(userId);
		try {
			boolean succeed = this.client.activateUserAccount(userId);
			return succeed;
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean deactivate(String userId) throws ClientException {
		checkUserId(userId);
		try {
			boolean succeed = this.client.deactivateUserAccount(userId);
			return succeed;
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean delete(String userId) throws ClientException {
		checkUserId(userId);
		try {
			StatusDTO status = this.client.deleteUserAccount(userId);
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

	// ------------------------------------------------------------------------------------------------
	// Helper methods
	// ------------------------------------------------------------------------------------------------
	/**
	 * Generate a unique id, for load balancing purpose, based on indexing properties for a user registry.
	 * 
	 * @param properties
	 * @return
	 */
	protected String getLoadBalanceId(Map<String, Object> properties) {
		String userRegistryName = (String) properties.get(OrbitConstants.USER_REGISTRY_NAME);
		String url = (String) properties.get(OrbitConstants.USER_REGISTRY_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.USER_REGISTRY_CONTEXT_ROOT);
		String key = url + "::" + contextRoot + "::" + userRegistryName;
		return key;
	}

	/**
	 * Convert CreateUserAccountRequest to DTO.
	 * 
	 * @param createUserAccountRequest
	 * @return
	 */
	protected UserAccountDTO toDTO(CreateUserAccountRequest createUserAccountRequest) {
		UserAccountDTO createUserAccountRequestDTO = new UserAccountDTO();
		createUserAccountRequestDTO.setUserId(createUserAccountRequest.getUserId());
		createUserAccountRequestDTO.setPassword(createUserAccountRequest.getPassword());
		createUserAccountRequestDTO.setEmail(createUserAccountRequest.getEmail());
		createUserAccountRequestDTO.setFirstName(createUserAccountRequest.getFirstName());
		createUserAccountRequestDTO.setLastName(createUserAccountRequest.getLastName());
		createUserAccountRequestDTO.setPhone(createUserAccountRequest.getPhone());
		return createUserAccountRequestDTO;
	}

	/**
	 * Convert UpdateUserAccountRequest to DTO.
	 * 
	 * @param updateUserAccountRequest
	 * @return
	 */
	protected UserAccountDTO toDTO(UpdateUserAccountRequest updateUserAccountRequest) {
		UserAccountDTO updateUserAccountRequestDTO = new UserAccountDTO();
		updateUserAccountRequestDTO.setUserId(updateUserAccountRequest.getUserId());
		updateUserAccountRequestDTO.setPassword(updateUserAccountRequest.getPassword());
		updateUserAccountRequestDTO.setEmail(updateUserAccountRequest.getEmail());
		updateUserAccountRequestDTO.setFirstName(updateUserAccountRequest.getFirstName());
		updateUserAccountRequestDTO.setLastName(updateUserAccountRequest.getLastName());
		updateUserAccountRequestDTO.setPhone(updateUserAccountRequest.getPhone());
		return updateUserAccountRequestDTO;
	}

	/**
	 * Convert DTO to UserAccountImpl.
	 * 
	 * @param dto
	 * @return
	 */
	protected UserAccountImpl toUserAccountImpl(UserAccountDTO dto) {
		UserAccountImpl impl = new UserAccountImpl();
		impl.setUserId(dto.getUserId());
		impl.setEmail(dto.getEmail());
		impl.setPassword(dto.getPassword());
		impl.setFirstName(dto.getFirstName());
		impl.setLastName(dto.getLastName());
		impl.setPhone(dto.getPhone());
		impl.setCreationTime(dto.getCreationTime());
		impl.setLastUpdateTime(dto.getLastUpdateTime());
		impl.setActivated(dto.isActivated());
		return impl;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

}

// ClientConfiguration clientConfig = getClientConfiguration(this.properties);
// this.client = new UserRegistryWSClient(clientConfig);

// /**
// * Get user registry client configuration.
// *
// * @param properties
// * @return
// */
// protected ClientConfiguration getClientConfiguration(Map<String, Object> properties) {
// String realm = (String) properties.get(OrbitConstants.REALM);
// String username = (String) properties.get(OrbitConstants.USERNAME);
// String url = (String) properties.get(OrbitConstants.USER_REGISTRY_HOST_URL);
// String contextRoot = (String) properties.get(OrbitConstants.USER_REGISTRY_CONTEXT_ROOT);
// return ClientConfiguration.create(realm, username, url, contextRoot);
// }
