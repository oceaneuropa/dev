package org.orbit.component.runtime.tier1.account.service;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.component.model.tier1.account.rto.UserAccount;
import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier1.account.persistence.UserAccountPersistence;
import org.orbit.component.runtime.tier1.account.persistence.UserAccountPersistenceImpl;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class UserRegistryServiceImpl implements UserRegistryService, LifecycleAware {

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected ServiceRegistration<?> serviceRegistry;
	protected UserAccountPersistence userAccountPersistence;

	public UserRegistryServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
	}

	@Override
	public void start(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".start()");

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}

		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_PASSWORD);

		update(properties);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(UserRegistryService.class, this, props);
	}

	@Override
	public void stop(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".stop()");

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
		System.out.println(getClass().getSimpleName() + ".updateProperties()");
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		this.properties = properties;

		String driver = (String) this.properties.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_DRIVER);
		String url = (String) this.properties.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_URL);
		String username = (String) this.properties.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_USERNAME);
		String password = (String) this.properties.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_PASSWORD);
		Properties databaseProperties = DatabaseUtil.getProperties(driver, url, username, password);

		this.userAccountPersistence = new UserAccountPersistenceImpl(databaseProperties);
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(OrbitConstants.COMPONENT_USER_REGISTRY_NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(OrbitConstants.COMPONENT_USER_REGISTRY_HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.properties.get(OrbitConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = (String) this.properties.get(OrbitConstants.COMPONENT_USER_REGISTRY_CONTEXT_ROOT);
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
	 * @param userId
	 * @throws ServerException
	 */
	protected void checkUserId(String userId) throws ServerException {
		if (userId == null || userId.isEmpty()) {
			throw new ServerException("400", "userId is empty.");
		}
	}

	/**
	 * 
	 * @param password
	 * @throws ServerException
	 */
	protected void checkPassword(String password) throws ServerException {
		if (password == null || password.isEmpty()) {
			throw new ServerException("400", "password is empty.");
		}
	}

	/**
	 * 
	 * @param email
	 * @throws ServerException
	 */
	protected void checkEmail(String email) throws ServerException {
		if (email == null || email.isEmpty()) {
			throw new ServerException("400", "email is empty.");
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
	public UserAccount getUserAccount(String userId) throws ServerException {
		checkUserId(userId);
		try {
			return this.userAccountPersistence.getUserAccount(userId);
		} catch (Exception e) {
			handleException(e);
		}
		return null;
	}

	@Override
	public boolean userAccountExists(String userId) throws ServerException {
		checkUserId(userId);
		try {
			return this.userAccountPersistence.userAccountExists(userId);
		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	@Override
	public boolean matchUsernamePassword(String userId, String password) throws ServerException {
		checkUserId(userId);
		try {
			UserAccount userAccount = this.userAccountPersistence.getUserAccount(userId);
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
		String userId = newAccountRequest.getUserId();
		String password = newAccountRequest.getPassword();
		String email = newAccountRequest.getEmail();
		String firstName = newAccountRequest.getFirstName();
		String lastName = newAccountRequest.getLastName();
		String phone = newAccountRequest.getPhone();

		checkUserId(userId);
		checkPassword(password);
		checkEmail(email);

		try {
			return this.userAccountPersistence.createUserAccount(userId, password, email, firstName, lastName, phone);
		} catch (Exception e) {
			handleException(e);
		}
		return null;
	}

	@Override
	public boolean updateUserAccount(UserAccount updateAccountRequest) throws ServerException {
		String userId = updateAccountRequest.getUserId();
		String newPassword = updateAccountRequest.getPassword();
		String newEmail = updateAccountRequest.getEmail();
		String newFirstName = updateAccountRequest.getFirstName();
		String newLastName = updateAccountRequest.getLastName();
		String newPhone = updateAccountRequest.getPhone();

		checkUserId(userId);

		boolean isUpdated = false;

		try {
			UserAccount userAccount = this.userAccountPersistence.getUserAccount(userId);
			if (userAccount == null) {
				throw new ServerException("404", "User account with userId '" + userId + "' is not found.");
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
					boolean succeed = this.userAccountPersistence.updatePassword(userId, newPassword);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (newEmail != null) {
				checkEmail(newEmail);
				boolean needToUpdate = (!newEmail.equals(oldEmail)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.userAccountPersistence.updateEmail(userId, newEmail);
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
					boolean succeed = this.userAccountPersistence.updateName(userId, newFirstName, newLastName);
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (newPhone != null) {
				if (!newPhone.equals(oldPhone)) {
					boolean succeed = this.userAccountPersistence.updatePhone(userId, newPhone);
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
	public boolean deleteUserAccount(String userId) throws ServerException {
		checkUserId(userId);
		try {
			return this.userAccountPersistence.deleteUserAccount(userId);
		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	@Override
	public boolean changePassword(String userId, String oldPassword, String newPassword) throws ServerException {
		checkUserId(userId);
		try {
			UserAccount userAccount = this.userAccountPersistence.getUserAccount(userId);
			if (userAccount == null) {
				throw new ServerException("404", "User account with userId '" + userId + "' is not found.");
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
			return this.userAccountPersistence.updatePassword(userId, newPassword);

		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	@Override
	public boolean isUserAccountActivated(String userId) throws ServerException {
		checkUserId(userId);
		try {
			UserAccount userAccount = this.userAccountPersistence.getUserAccount(userId);
			if (userAccount == null) {
				throw new ServerException("404", "User account with userId '" + userId + "' is not found.");
			}
			return userAccount.isActivated();

		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	@Override
	public boolean activateUserAccount(String userId) throws ServerException {
		checkUserId(userId);
		try {
			UserAccount userAccount = this.userAccountPersistence.getUserAccount(userId);
			if (userAccount == null) {
				throw new ServerException("404", "User account with userId '" + userId + "' is not found.");
			}
			boolean activated = userAccount.isActivated();
			if (activated) {
				// already activated
				return true;
			}
			return this.userAccountPersistence.setUserAccountActivated(userId, true);

		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	@Override
	public boolean deactivateUserAccount(String userId) throws ServerException {
		checkUserId(userId);
		try {
			UserAccount userAccount = this.userAccountPersistence.getUserAccount(userId);
			if (userAccount == null) {
				throw new ServerException("404", "User account with userId '" + userId + "' is not found.");
			}
			boolean activated = userAccount.isActivated();
			if (!activated) {
				// already deactivated
				return true;
			}
			return this.userAccountPersistence.setUserAccountActivated(userId, false);

		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

}
