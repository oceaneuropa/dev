package org.orbit.component.server.tier2.appstore.service.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.component.model.tier2.appstore.AppManifestRTO;
import org.orbit.component.model.tier2.appstore.AppQueryRTO;
import org.orbit.component.model.tier2.appstore.AppStoreException;
import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier2.appstore.service.AppStoreService;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class AppStoreServiceDatabaseImpl implements AppStoreService {

	protected Map<Object, Object> configProps = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;

	protected AppCategoryTableHandler categoryTableHandler;
	protected AppMetadataTableHandler appTableHandler;

	public AppStoreServiceDatabaseImpl() {
	}

	/**
	 * Start AppStoreService
	 * 
	 */
	public void start(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".start()");

		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_APP_STORE_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_APP_STORE_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_APP_STORE_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_APP_STORE_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_APP_STORE_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_APP_STORE_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_APP_STORE_JDBC_PASSWORD);

		updateProperties(configProps);

		String database = null;
		try {
			database = DatabaseUtil.getDatabase(this.databaseProperties);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assert (database != null) : "database name cannot be retrieved.";

		this.categoryTableHandler = AppCategoryTableHandler.INSTANCE;
		this.appTableHandler = new AppMetadataTableHandler(database);

		initialize();

		// Register AppStoreService
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(AppStoreService.class, this, props);
	}

	/**
	 * Stop AppStoreService
	 * 
	 */
	public void stop() {
		System.out.println(getClass().getSimpleName() + ".stop()");

		// Unregister AppStoreService
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
		String name = (String) configProps.get(OrbitConstants.COMPONENT_APP_STORE_NAME);
		String hostURL = (String) configProps.get(OrbitConstants.COMPONENT_APP_STORE_HOST_URL);
		String contextRoot = (String) configProps.get(OrbitConstants.COMPONENT_APP_STORE_CONTEXT_ROOT);
		String jdbcDriver = (String) configProps.get(OrbitConstants.COMPONENT_APP_STORE_JDBC_DRIVER);
		String jdbcURL = (String) configProps.get(OrbitConstants.COMPONENT_APP_STORE_JDBC_URL);
		String jdbcUsername = (String) configProps.get(OrbitConstants.COMPONENT_APP_STORE_JDBC_USERNAME);
		String jdbcPassword = (String) configProps.get(OrbitConstants.COMPONENT_APP_STORE_JDBC_PASSWORD);

		System.out.println(OrbitConstants.ORBIT_HOST_URL + " = " + globalHostURL);
		System.out.println(OrbitConstants.COMPONENT_APP_STORE_NAME + " = " + name);
		System.out.println(OrbitConstants.COMPONENT_APP_STORE_HOST_URL + " = " + hostURL);
		System.out.println(OrbitConstants.COMPONENT_APP_STORE_CONTEXT_ROOT + " = " + contextRoot);
		System.out.println(OrbitConstants.COMPONENT_APP_STORE_JDBC_DRIVER + " = " + jdbcDriver);
		System.out.println(OrbitConstants.COMPONENT_APP_STORE_JDBC_URL + " = " + jdbcURL);
		System.out.println(OrbitConstants.COMPONENT_APP_STORE_JDBC_USERNAME + " = " + jdbcUsername);
		System.out.println(OrbitConstants.COMPONENT_APP_STORE_JDBC_PASSWORD + " = " + jdbcPassword);

		this.configProps = configProps;
		this.databaseProperties = getConnectionProperties(this.configProps);
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	protected synchronized Properties getConnectionProperties(Map<Object, Object> props) {
		String driver = (String) this.configProps.get(OrbitConstants.COMPONENT_APP_STORE_JDBC_DRIVER);
		String url = (String) this.configProps.get(OrbitConstants.COMPONENT_APP_STORE_JDBC_URL);
		String username = (String) this.configProps.get(OrbitConstants.COMPONENT_APP_STORE_JDBC_USERNAME);
		String password = (String) this.configProps.get(OrbitConstants.COMPONENT_APP_STORE_JDBC_PASSWORD);
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	protected Connection getConnection() {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	/**
	 * Initialize database tables.
	 */
	public void initialize() {
		Connection conn = DatabaseUtil.getConnection(this.databaseProperties);
		try {
			if (this.categoryTableHandler != null) {
				DatabaseUtil.initialize(conn, this.categoryTableHandler);
			}
			if (this.appTableHandler != null) {
				DatabaseUtil.initialize(conn, this.appTableHandler);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	@Override
	public String getName() {
		String name = (String) this.configProps.get(OrbitConstants.COMPONENT_APP_STORE_NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.configProps.get(OrbitConstants.COMPONENT_APP_STORE_HOST_URL);
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
		String contextRoot = (String) this.configProps.get(OrbitConstants.COMPONENT_APP_STORE_CONTEXT_ROOT);
		return contextRoot;
	}

	/**
	 * 
	 * @param e
	 * @throws AppStoreException
	 */
	protected void handleSQLException(SQLException e) throws AppStoreException {
		e.printStackTrace();
		throw new AppStoreException(StatusDTO.RESP_500, e.getMessage(), e);
	}

	@Override
	public List<AppManifestRTO> getApps(String namespace, String categoryId) throws AppStoreException {
		Connection conn = getConnection();
		try {
			AppQueryRTO query = new AppQueryRTO();
			query.setNamespace(namespace);
			query.setCategoryId(categoryId);
			return this.appTableHandler.getApps(conn, query);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public List<AppManifestRTO> getApps(AppQueryRTO query) throws AppStoreException {
		Connection conn = getConnection();
		try {
			return this.appTableHandler.getApps(conn, query);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public boolean appExists(String appId) throws AppStoreException {
		Connection conn = getConnection();
		try {
			return this.appTableHandler.appExists(conn, appId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public AppManifestRTO getApp(String appId) throws AppStoreException {
		Connection conn = getConnection();
		try {
			return this.appTableHandler.getApp(conn, appId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public AppManifestRTO addApp(AppManifestRTO createAppRequest) throws AppStoreException {
		Connection conn = getConnection();

		if (appExists(createAppRequest.getAppId())) {
			throw new AppStoreException(StatusDTO.RESP_500, "App already exists.");
		}

		try {
			return this.appTableHandler.insert(conn, createAppRequest);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public boolean updateApp(AppManifestRTO updateAppRequest) throws AppStoreException {
		Connection conn = getConnection();
		try {
			return this.appTableHandler.update(conn, updateAppRequest);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean deleteApp(String appId) throws AppStoreException {
		Connection conn = getConnection();
		try {
			return this.appTableHandler.delete(conn, appId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public byte[] downloadApp(String appId) throws AppStoreException {
		Connection conn = getConnection();
		try {
			return this.appTableHandler.getAppContent(conn, appId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public InputStream downloadAppInputStream(String appId) throws AppStoreException {
		Connection conn = getConnection();
		try {
			return this.appTableHandler.getAppContentInputStream(conn, appId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public boolean uploadApp(String appId, String fileName, InputStream fileInputStream) throws AppStoreException {
		Connection conn = getConnection();
		try {
			boolean succeed = this.appTableHandler.setAppContent(conn, appId, fileInputStream);
			if (succeed) {
				this.appTableHandler.updateFileName(conn, appId, fileName);
			}
			return succeed;
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

}
