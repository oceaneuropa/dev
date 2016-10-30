package org.orbit.component.server.appstore.service;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.component.model.appstore.exception.AppStoreException;
import org.orbit.component.model.appstore.runtime.AppManifestRTO;
import org.orbit.component.model.appstore.runtime.AppQueryRTO;
import org.orbit.component.server.appstore.handler.AppCategoryTableHandler;
import org.orbit.component.server.appstore.handler.AppMetadataTableHandler;
import org.origin.common.jdbc.DatabaseUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class AppStoreServiceDatabaseImpl implements AppStoreService {

	protected Map<Object, Object> props;
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceReg;

	protected AppCategoryTableHandler categoryTableHandler;
	protected AppMetadataTableHandler appTableHandler;

	/**
	 * 
	 * @param props
	 */
	public AppStoreServiceDatabaseImpl(Map<Object, Object> props) {
		assert (props != null) : "props is null";
		this.props = props;
		this.databaseProperties = getConnectionProperties(this.props);

		String database = null;
		try {
			database = DatabaseUtil.getDatabase(this.databaseProperties);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assert (database != null) : "database name cannot be retrieved.";

		this.categoryTableHandler = AppCategoryTableHandler.INSTANCE;
		this.appTableHandler = new AppMetadataTableHandler(database);
	}

	/**
	 * Initialize database tables.
	 */
	public void initializeTables() {
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

	/**
	 * 
	 * @param props
	 */
	public synchronized void update(Map<Object, Object> props) {
		assert (props != null) : "props is null";
		this.props = props;
		this.databaseProperties = getConnectionProperties(this.props);
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	protected synchronized Properties getConnectionProperties(Map<Object, Object> props) {
		String driver = (String) this.props.get("appstore.jdbc.driver");
		String url = (String) this.props.get("appstore.jdbc.url");
		String username = (String) this.props.get("appstore.jdbc.username");
		String password = (String) this.props.get("appstore.jdbc.password");
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	protected Connection getConnection() {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	/**
	 * Start AppStoreService
	 * 
	 */
	public void start(BundleContext bundleContext) {
		// Register AppStoreService
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceReg = bundleContext.registerService(AppStoreService.class, this, props);
	}

	/**
	 * Stop AppStoreService
	 * 
	 */
	public void stop() {
		// Unregister AppStoreService
		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}
	}

	/**
	 * 
	 * @param e
	 * @throws AppStoreException
	 */
	protected void handleSQLException(SQLException e) throws AppStoreException {
		e.printStackTrace();
		throw new AppStoreException("500", e.getMessage(), e);
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
			throw new AppStoreException("500", "App already exists.");
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
