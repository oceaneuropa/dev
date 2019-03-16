package org.orbit.component.runtime.tier1.account.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.model.account.UserAccount;
import org.orbit.component.runtime.tier1.account.service.UserAccountPersistence;
import org.orbit.component.runtime.tier1.account.service.UserAccountPersistenceFactory;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.orbit.platform.sdk.http.AccessTokenSupport;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.origin.common.util.UUIDUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistryServiceImpl implements UserRegistryService, LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(UserRegistryServiceImpl.class);

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected ServiceRegistration<?> serviceRegistry;
	protected UserAccountPersistence userAccountPersistence;
	protected AccessTokenSupport accessTokenSupport;

	/**
	 * 
	 * @param initProperties
	 */
	public UserRegistryServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.accessTokenSupport = new AccessTokenSupport(ComponentConstants.TOKEN_PROVIDER__ORBIT, OrbitRoles.USER_ACCOUNTS_ADMIN);
	}

	@Override
	public String getAccessToken() {
		String tokenValue = this.accessTokenSupport.getAccessToken();
		return tokenValue;
	}

	@Override
	public void start(BundleContext bundleContext) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}

		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_USER_REGISTRY_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_USER_REGISTRY_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_USER_REGISTRY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_USER_REGISTRY_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_USER_REGISTRY_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_USER_REGISTRY_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_USER_REGISTRY_JDBC_PASSWORD);

		update(properties);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(UserRegistryService.class, this, props);
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}
	}

	/**
	 * 
	 * @param properties
	 */
	public synchronized void update(Map<Object, Object> properties) {
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		this.properties = properties;

		String driver = (String) this.properties.get(ComponentConstants.COMPONENT_USER_REGISTRY_JDBC_DRIVER);
		String url = (String) this.properties.get(ComponentConstants.COMPONENT_USER_REGISTRY_JDBC_URL);
		String username = (String) this.properties.get(ComponentConstants.COMPONENT_USER_REGISTRY_JDBC_USERNAME);
		String password = (String) this.properties.get(ComponentConstants.COMPONENT_USER_REGISTRY_JDBC_PASSWORD);
		Properties databaseProperties = DatabaseUtil.getProperties(driver, url, username, password);

		try {
			this.userAccountPersistence = UserAccountPersistenceFactory.getInstance().create(databaseProperties);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(ComponentConstants.COMPONENT_USER_REGISTRY_NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(ComponentConstants.COMPONENT_USER_REGISTRY_HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.properties.get(ComponentConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = (String) this.properties.get(ComponentConstants.COMPONENT_USER_REGISTRY_CONTEXT_ROOT);
		return contextRoot;
	}

	/**
	 * 
	 * @param e
	 * @throws ServerException
	 */
	protected void handleException(Exception e) throws ServerException {
		e.printStackTrace();
		throw new ServerException(StatusDTO.RESP_500, e.getMessage(), e);
	}

	/**
	 * 
	 * @param accountId
	 * @throws ServerException
	 */
	protected void checkAccountId(String accountId) throws ServerException {
		if (accountId == null || accountId.isEmpty()) {
			throw new ServerException(StatusDTO.RESP_400, "accountId is empty.");
		}
	}

	/**
	 * 
	 * @param username
	 * @throws ServerException
	 */
	protected void checkUsername(String username) throws ServerException {
		if (username == null || username.isEmpty()) {
			throw new ServerException(StatusDTO.RESP_400, "username is empty.");
		}
	}

	/**
	 * 
	 * @param email
	 * @throws ServerException
	 */
	protected void checkEmail(String email) throws ServerException {
		if (email == null || email.isEmpty()) {
			throw new ServerException(StatusDTO.RESP_400, "email is empty.");
		}
	}

	/**
	 * 
	 * @param password
	 * @throws ServerException
	 */
	protected void checkPassword(String password) throws ServerException {
		if (password == null || password.isEmpty()) {
			throw new ServerException(StatusDTO.RESP_400, "password is empty.");
		}
	}

	@Override
	public List<UserAccount> getUserAccounts() throws ServerException {
		try {
			return this.userAccountPersistence.getUserAccounts();
		} catch (Exception e) {
			handleException(e);
		}
		return null;
	}

	@Override
	public UserAccount getUserAccountByAccountId(String accountId) throws ServerException {
		checkAccountId(accountId);
		try {
			return this.userAccountPersistence.getUserAccountByAccountId(accountId);
		} catch (Exception e) {
			handleException(e);
		}
		return null;
	}

	@Override
	public UserAccount getUserAccountByUsername(String username) throws ServerException {
		checkUsername(username);
		try {
			return this.userAccountPersistence.getUserAccountByUsername(username);
		} catch (Exception e) {
			handleException(e);
		}
		return null;
	}

	@Override
	public boolean accountIdExists(String accountId) throws ServerException {
		checkUsername(accountId);
		try {
			return this.userAccountPersistence.accountIdExists(accountId);
		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	@Override
	public boolean usernameExists(String username) throws ServerException {
		checkUsername(username);
		try {
			return this.userAccountPersistence.usernameExists(username);
		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	@Override
	public boolean emailExists(String email) throws ServerException {
		checkEmail(email);
		try {
			return this.userAccountPersistence.emailExists(email);
		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	@Override
	public boolean matchUsernamePassword(String username, String password) throws ServerException {
		checkUsername(username);
		try {
			UserAccount userAccount = this.userAccountPersistence.getUserAccountByUsername(username);
			if (userAccount != null) {
				String existingPassword = userAccount.getPassword();

				if (password == null) {
					password = "";
				}
				if (existingPassword == null) {
					existingPassword = "";
				}
				if (password.equals(existingPassword)) {
					return true;
				}
			}
		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	@Override
	public UserAccount registerUserAccount(UserAccount newAccountRequest) throws ServerException {
		String username = newAccountRequest.getUsername();
		String password = newAccountRequest.getPassword();
		String email = newAccountRequest.getEmail();
		String firstName = newAccountRequest.getFirstName();
		String lastName = newAccountRequest.getLastName();
		String phone = newAccountRequest.getPhone();

		checkUsername(username);
		checkPassword(password);
		checkEmail(email);

		try {
			String accountId = UUIDUtil.generateBase64EncodedUUID();
			boolean acountIdExists = this.userAccountPersistence.accountIdExists(accountId);
			if (acountIdExists) {
				int retryCount = 0;
				int maxRetry = 10;
				while (acountIdExists) {
					accountId = UUIDUtil.generateBase64EncodedUUID();
					acountIdExists = this.userAccountPersistence.accountIdExists(accountId);
					if (!acountIdExists) {
						break;
					}
					retryCount++;
					if (retryCount == maxRetry) {
						throw new ServerException(StatusDTO.RESP_500, "Cannot create account Id.");
					}

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			boolean usernameExists = this.userAccountPersistence.usernameExists(username);
			if (usernameExists) {
				throw new ServerException(StatusDTO.RESP_400, "Username already exists.");
			}
			boolean emailExists = this.userAccountPersistence.emailExists(email);
			if (emailExists) {
				throw new ServerException(StatusDTO.RESP_400, "Email already exists.");
			}

			return this.userAccountPersistence.createUserAccount(accountId, username, password, email, firstName, lastName, phone);

		} catch (Exception e) {
			handleException(e);
		}
		return null;
	}

	@Override
	public boolean updateUserAccount(UserAccount updateAccountRequest) throws ServerException {
		String accountId = updateAccountRequest.getAccountId();
		String username = updateAccountRequest.getUsername();
		String newPassword = updateAccountRequest.getPassword();
		String newEmail = updateAccountRequest.getEmail();
		String newFirstName = updateAccountRequest.getFirstName();
		String newLastName = updateAccountRequest.getLastName();
		String newPhone = updateAccountRequest.getPhone();

		boolean isUpdated = false;

		try {
			UserAccount userAccount = this.userAccountPersistence.getUserAccountByAccountId(accountId);
			if (userAccount == null) {
				userAccount = this.userAccountPersistence.getUserAccountByUsername(username);
			}
			if (userAccount == null) {
				throw new ServerException("404", "User account is not found.");
			}

			String oldPassword = userAccount.getPassword();
			String oldEmail = userAccount.getEmail();
			String oldFirstName = userAccount.getFirstName();
			String oldLastName = userAccount.getLastName();
			String oldPhone = userAccount.getPhone();

			if (newPassword != null) {
				checkPassword(newPassword);
				boolean needToUpdate = (!newPassword.equals(oldPassword)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.userAccountPersistence.updatePassword(accountId, newPassword);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (newEmail != null) {
				checkEmail(newEmail);
				boolean needToUpdate = (!newEmail.equals(oldEmail)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.userAccountPersistence.updateEmail(accountId, newEmail);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (newFirstName != null || newLastName != null) {
				boolean needToUpdate = false;
				if (newFirstName != null && !newFirstName.equals(oldFirstName)) {
					needToUpdate = true;
				}
				if (newLastName != null && !newLastName.equals(oldLastName)) {
					needToUpdate = true;
				}
				if (needToUpdate) {
					if (newFirstName == null) {
						newFirstName = oldFirstName;
					}
					if (newLastName == null) {
						newLastName = oldLastName;
					}
					boolean succeed = this.userAccountPersistence.updateName(accountId, newFirstName, newLastName);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (newPhone != null) {
				if (!newPhone.equals(oldPhone)) {
					boolean succeed = this.userAccountPersistence.updatePhone(accountId, newPhone);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

		} catch (Exception e) {
			handleException(e);
		}

		return isUpdated;
	}

	@Override
	public boolean deleteUserAccount(String accountId) throws ServerException {
		checkAccountId(accountId);
		try {
			return this.userAccountPersistence.deleteUserAccount(accountId);
		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	@Override
	public boolean changePassword(String accountId, String oldPassword, String newPassword) throws ServerException {
		checkAccountId(accountId);
		try {
			UserAccount userAccount = this.userAccountPersistence.getUserAccountByAccountId(accountId);
			if (userAccount == null) {
				throw new ServerException("404", "User account with accountId '" + accountId + "' is not found.");
			}

			boolean matchOldPassword = false;
			String existingPassword = userAccount.getPassword();
			if (existingPassword == null) {
				existingPassword = "";
			}
			if (oldPassword == null) {
				oldPassword = "";
			}
			if (existingPassword.equals(oldPassword)) {
				matchOldPassword = true;
			}
			if (!matchOldPassword) {
				throw new ServerException("404", "Old password is incorrect.");
			}
			return this.userAccountPersistence.updatePassword(accountId, newPassword);

		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	@Override
	public boolean isUserAccountActivated(String accountId) throws ServerException {
		checkAccountId(accountId);
		try {
			UserAccount userAccount = this.userAccountPersistence.getUserAccountByAccountId(accountId);
			if (userAccount == null) {
				throw new ServerException("404", "User account is not found.");
			}
			return userAccount.isActivated();

		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	@Override
	public boolean activateUserAccount(String accountId) throws ServerException {
		checkAccountId(accountId);
		try {
			UserAccount userAccount = this.userAccountPersistence.getUserAccountByAccountId(accountId);
			if (userAccount == null) {
				throw new ServerException("404", "User account is not found.");
			}
			boolean activated = userAccount.isActivated();
			if (activated) {
				// already activated
				return true;
			}
			return this.userAccountPersistence.setActivated(accountId, true);

		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	@Override
	public boolean deactivateUserAccount(String accountId) throws ServerException {
		checkAccountId(accountId);
		try {
			UserAccount userAccount = this.userAccountPersistence.getUserAccountByAccountId(accountId);
			if (userAccount == null) {
				throw new ServerException("404", "User account is not found.");
			}
			boolean activated = userAccount.isActivated();
			if (!activated) {
				// already deactivated
				return true;
			}
			return this.userAccountPersistence.setActivated(accountId, false);

		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

}
