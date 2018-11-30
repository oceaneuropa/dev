package other.orbit.component.connector.tier1.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.component.api.tier1.account.CreateUserAccountRequest;
import org.orbit.component.api.tier1.account.UpdateUserAccountRequest;
import org.orbit.component.api.tier1.account.UserAccount;
import org.orbit.component.api.tier1.account.UserAccountClient;
import org.orbit.component.connector.tier1.account.UserAccountImpl;
import org.orbit.component.connector.tier1.account.UserAccountWSClient;
import org.orbit.component.model.tier1.account.UserAccountDTO;
import org.orbit.infra.api.InfraConstants;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.StatusDTO;

public class UserRegistryImplV1 implements UserAccountClient {

	protected Map<String, Object> properties;
	protected UserAccountWSClient client;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public UserRegistryImplV1(ServiceConnector<UserAccountClient> connector, Map<String, Object> properties) {
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
		ServiceConnector<UserAccountClient> connector = getAdapter(ServiceConnector.class);
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
		WSClientConfiguration config = WSClientConfiguration.create(this.properties);
		this.client = new UserAccountWSClient(config);
	}

	@Override
	public ServiceMetadata getMetadata() throws ClientException {
		return null;
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(InfraConstants.SERVICE__NAME);
		return name;
	}

	@Override
	public String getURL() {
		String url = (String) properties.get(WSClientConstants.URL);
		return url;
	}

	@Override
	public boolean isProxy() {
		return false;
	}

	@Override
	public boolean ping() {
		return this.client.doPing();
	}

	@Override
	public String echo(String message) throws ClientException {
		return null;
	}

	@Override
	public Response sendRequest(Request request) throws ClientException {
		return null;
	}

	/**
	 * 
	 * @param accountId
	 * @throws ClientException
	 */
	protected void checkAccountId(String accountId) throws ClientException {
		if (accountId == null || accountId.isEmpty()) {
			throw new ClientException(400, "accountId is empty.");
		}
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
			List<UserAccountDTO> userAccountDTOs = this.client.getList();
			for (UserAccountDTO userAccountDTO : userAccountDTOs) {
				userAccounts.add(toUserAccountImpl(userAccountDTO));
			}
		} catch (ClientException e) {
			throw e;
		}
		return userAccounts.toArray(new UserAccount[userAccounts.size()]);
	}

	@Override
	public UserAccount getUserAccount(String accountId) throws ClientException {
		checkAccountId(accountId);
		UserAccount userAccount = null;
		try {
			UserAccountDTO userAccountDTO = this.client.get(accountId);
			if (userAccountDTO != null) {
				userAccount = toUserAccountImpl(userAccountDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return userAccount;
	}

	@Override
	public boolean usernameExists(String username) throws ClientException {
		checkUsername(username);
		try {
			return this.client.exists("username", username);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean emailExists(String email) throws ClientException {
		checkEmail(email);
		try {
			return this.client.exists("email", email);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean register(CreateUserAccountRequest createUserAccountRequest) throws ClientException {
		String username = createUserAccountRequest.getUsername();
		checkUsername(username);
		try {
			UserAccountDTO createUserAccountRequestDTO = toDTO(createUserAccountRequest);
			StatusDTO status = this.client.create(createUserAccountRequestDTO);
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
		String username = updateUserAccountRequest.getUsername();
		checkUsername(username);
		try {
			UserAccountDTO updateUserAccountRequestDTO = toDTO(updateUserAccountRequest);
			StatusDTO status = this.client.update(updateUserAccountRequestDTO);
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
			return this.client.changePassword(username, oldPassword, newPassword);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean isActivated(String username) throws ClientException {
		checkUsername(username);
		try {
			return this.client.isActivated(username);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean activate(String username) throws ClientException {
		checkUsername(username);
		try {
			boolean succeed = this.client.activate(username);
			return succeed;
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean deactivate(String username) throws ClientException {
		checkUsername(username);
		try {
			boolean succeed = this.client.deactivate(username);
			return succeed;
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean delete(String username) throws ClientException {
		checkUsername(username);
		try {
			StatusDTO status = this.client.delete(username);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
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
		String userRegistryName = (String) properties.get(InfraConstants.SERVICE__NAME);
		String url = (String) properties.get(InfraConstants.SERVICE__HOST_URL);
		String contextRoot = (String) properties.get(InfraConstants.SERVICE__CONTEXT_ROOT);
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
		createUserAccountRequestDTO.setUsername(createUserAccountRequest.getUsername());
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
		updateUserAccountRequestDTO.setUsername(updateUserAccountRequest.getUsername());
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
		impl.setUsername(dto.getUsername());
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
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
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
