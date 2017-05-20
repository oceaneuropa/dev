package org.orbit.component.server.tier1.account.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.component.model.tier1.account.UserAccountRTO;
import org.orbit.component.model.tier1.account.UserRegistryException;
import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier1.account.handler.UserAccountTableHandler;
import org.orbit.component.server.tier1.account.service.UserRegistryService;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class UserRegistryServiceDatabaseImpl implements UserRegistryService {

	protected Map<Object, Object> configProps = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;

	protected UserAccountTableHandler userAccountTableHandler = UserAccountTableHandler.INSTANCE;

	public UserRegistryServiceDatabaseImpl() {
	}

	/**
	 * Start UserRegistryService
	 * 
	 */
	public void start(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".start()");

		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_USER_REGISTRY_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_USER_REGISTRY_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_USER_REGISTRY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_PASSWORD);

		updateProperties(configProps);

		initialize();

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(UserRegistryService.class, this, props);
	}

	/**
	 * Stop UserRegistryService
	 * 
	 */
	public void stop() {
		System.out.println(getClass().getSimpleName() + ".stop()");

		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}
	}

	/**
	 * 
	 * @param configProps
	 */
	public synchronized void updateProperties(Map<Object, Object> configProps) {
		System.out.println(getClass().getSimpleName() + ".updateProperties()");

		if (configProps == null) {
			configProps = new HashMap<Object, Object>();
		}

		String globalHostURL = (String) configProps.get(OrbitConstants.ORBIT_HOST_URL);
		String name = (String) configProps.get(OrbitConstants.COMPONENT_USER_REGISTRY_NAME);
		String hostURL = (String) configProps.get(OrbitConstants.COMPONENT_USER_REGISTRY_HOST_URL);
		String contextRoot = (String) configProps.get(OrbitConstants.COMPONENT_USER_REGISTRY_CONTEXT_ROOT);
		String jdbcDriver = (String) configProps.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_DRIVER);
		String jdbcURL = (String) configProps.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_URL);
		String jdbcUsername = (String) configProps.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_USERNAME);
		String jdbcPassword = (String) configProps.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_PASSWORD);

		System.out.println(OrbitConstants.ORBIT_HOST_URL + " = " + globalHostURL);
		System.out.println(OrbitConstants.COMPONENT_USER_REGISTRY_NAME + " = " + name);
		System.out.println(OrbitConstants.COMPONENT_USER_REGISTRY_HOST_URL + " = " + hostURL);
		System.out.println(OrbitConstants.COMPONENT_USER_REGISTRY_CONTEXT_ROOT + " = " + contextRoot);
		System.out.println(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_DRIVER + " = " + jdbcDriver);
		System.out.println(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_URL + " = " + jdbcURL);
		System.out.println(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_USERNAME + " = " + jdbcUsername);
		System.out.println(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_PASSWORD + " = " + jdbcPassword);

		this.configProps = configProps;
		this.databaseProperties = getConnectionProperties(this.configProps);
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	protected synchronized Properties getConnectionProperties(Map<Object, Object> props) {
		String driver = (String) this.configProps.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_DRIVER);
		String url = (String) this.configProps.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_URL);
		String username = (String) this.configProps.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_USERNAME);
		String password = (String) this.configProps.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_PASSWORD);
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	/**
	 * Initialize database tables.
	 */
	public void initialize() {
		Connection conn = DatabaseUtil.getConnection(this.databaseProperties);
		try {
			DatabaseUtil.initialize(conn, UserAccountTableHandler.INSTANCE);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	protected Connection getConnection() {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	@Override
	public String getName() {
		String name = (String) this.configProps.get(OrbitConstants.COMPONENT_USER_REGISTRY_NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.configProps.get(OrbitConstants.COMPONENT_USER_REGISTRY_HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.configProps.get(OrbitConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = (String) this.configProps.get(OrbitConstants.COMPONENT_USER_REGISTRY_CONTEXT_ROOT);
		return contextRoot;
	}

	/**
	 * 
	 * @param e
	 * @throws UserRegistryException
	 */
	protected void handleSQLException(SQLException e) throws UserRegistryException {
		e.printStackTrace();
		throw new UserRegistryException(StatusDTO.RESP_500, e.getMessage(), e);
	}

	/**
	 * 
	 * @param userId
	 * @throws UserRegistryException
	 */
	protected void checkUserId(String userId) throws UserRegistryException {
		if (userId == null || userId.isEmpty()) {
			throw new UserRegistryException("400", "userId is empty.");
		}
	}

	/**
	 * 
	 * @param password
	 * @throws UserRegistryException
	 */
	protected void checkPassword(String password) throws UserRegistryException {
		if (password == null || password.isEmpty()) {
			throw new UserRegistryException("400", "password is empty.");
		}
	}

	/**
	 * 
	 * @param email
	 * @throws UserRegistryException
	 */
	protected void checkEmail(String email) throws UserRegistryException {
		if (email == null || email.isEmpty()) {
			throw new UserRegistryException("400", "email is empty.");
		}
	}

	@Override
	public List<UserAccountRTO> getUserAccounts() throws UserRegistryException {
		Connection conn = getConnection();
		try {
			return this.userAccountTableHandler.getUserAccounts(conn);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public UserAccountRTO getUserAccount(String userId) throws UserRegistryException {
		checkUserId(userId);

		Connection conn = getConnection();
		try {
			return this.userAccountTableHandler.getUserAccount(conn, userId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public boolean userAccountExists(String userId) throws UserRegistryException {
		checkUserId(userId);

		Connection conn = getConnection();
		try {
			return this.userAccountTableHandler.userAccountExists(conn, userId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public UserAccountRTO registerUserAccount(UserAccountRTO newAccountRequest) throws UserRegistryException {
		String userId = newAccountRequest.getUserId();
		String password = newAccountRequest.getPassword();
		String email = newAccountRequest.getEmail();
		String firstName = newAccountRequest.getFirstName();
		String lastName = newAccountRequest.getLastName();
		String phone = newAccountRequest.getPhone();
		Date creationTime = new Date();
		Date lastUpdateTime = creationTime;

		checkUserId(userId);
		checkPassword(password);
		checkEmail(email);

		Connection conn = getConnection();
		try {
			return this.userAccountTableHandler.registerUserAccount(conn, userId, password, email, firstName, lastName, phone, creationTime, lastUpdateTime);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public boolean updateUserAccount(UserAccountRTO updateAccountRequest) throws UserRegistryException {
		String userId = updateAccountRequest.getUserId();
		String newPassword = updateAccountRequest.getPassword();
		String newEmail = updateAccountRequest.getEmail();
		String newFirstName = updateAccountRequest.getFirstName();
		String newLastName = updateAccountRequest.getLastName();
		String newPhone = updateAccountRequest.getPhone();

		checkUserId(userId);

		boolean isUpdated = false;

		Connection conn = getConnection();
		try {
			UserAccountRTO userAccount = this.userAccountTableHandler.getUserAccount(conn, userId);
			if (userAccount == null) {
				throw new UserRegistryException("404", "User account with userId '" + userId + "' is not found.");
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
					boolean succeed = this.userAccountTableHandler.updatePassword(conn, userId, newPassword, new Date());
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (newEmail != null) {
				checkEmail(newEmail);
				boolean needToUpdate = (!newEmail.equals(oldEmail)) ? true : false;
				if (needToUpdate) {
					boolean succeed = this.userAccountTableHandler.updateEmail(conn, userId, newEmail, new Date());
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
					boolean succeed = this.userAccountTableHandler.updateName(conn, userId, newFirstName, newLastName, new Date());
					if (succeed) {
						isUpdated = true;
					}
				}
			}

			if (newPhone != null) {
				if (!newPhone.equals(oldPhone)) {
					boolean succeed = this.userAccountTableHandler.updatePhone(conn, userId, newPhone, new Date());
					if (succeed) {
						isUpdated = true;
					}
				}
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		return isUpdated;
	}

	@Override
	public boolean deleteUserAccount(String userId) throws UserRegistryException {
		checkUserId(userId);

		Connection conn = getConnection();
		try {
			return this.userAccountTableHandler.deleteUserAccount(conn, userId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean isUserAccountActivated(String userId) throws UserRegistryException {
		checkUserId(userId);

		Connection conn = getConnection();
		try {
			UserAccountRTO userAccount = this.userAccountTableHandler.getUserAccount(conn, userId);
			if (userAccount == null) {
				throw new UserRegistryException("404", "User account with userId '" + userId + "' is not found.");
			}
			return userAccount.isActivated();

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean activateUserAccount(String userId) throws UserRegistryException {
		checkUserId(userId);

		Connection conn = getConnection();
		try {
			boolean exists = this.userAccountTableHandler.userAccountExists(conn, userId);
			if (!exists) {
				throw new UserRegistryException("404", "User account with userId '" + userId + "' is not found.");
			}
			return this.userAccountTableHandler.setUserAccountActivated(conn, userId, true);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean deactivateUserAccount(String userId) throws UserRegistryException {
		checkUserId(userId);

		Connection conn = getConnection();
		try {
			boolean exists = this.userAccountTableHandler.userAccountExists(conn, userId);
			if (!exists) {
				throw new UserRegistryException("404", "User account with userId '" + userId + "' is not found.");
			}
			return this.userAccountTableHandler.setUserAccountActivated(conn, userId, false);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

}