package org.orbit.infra.runtime.configregistry.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistry;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryMetadata;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.util.ConfigRegistryConfigPropertiesHandler;
import org.orbit.platform.sdk.http.AccessTokenProvider;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.event.PropertyChangeEvent;
import org.origin.common.event.PropertyChangeListener;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.editpolicy.ServiceEditPoliciesImpl;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.UUIDUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ConfigRegistryServiceImpl implements ConfigRegistryService, LifecycleAware, PropertyChangeListener {

	public static ConfigRegistry[] EMPTY_CONFIG_REGISTRIES = new ConfigRegistry[0];

	protected Map<Object, Object> initProperties;
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;
	protected ServiceEditPolicies wsEditPolicies;

	// protected Map<String, ConfigRegistry> configRegistryMap = new HashMap<String, ConfigRegistry>();
	protected AccessTokenProvider accessTokenSupport;

	/**
	 * 
	 * @param initProperties
	 */
	public ConfigRegistryServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.wsEditPolicies = new ServiceEditPoliciesImpl(ConfigRegistryService.class, this);
		this.accessTokenSupport = new AccessTokenProvider(InfraConstants.TOKEN_PROVIDER__ORBIT, OrbitRoles.CONFIG_REGISTRY_ADMIN);
	}

	/** AccessTokenAware */
	@Override
	public String getAccessToken() {
		String tokenValue = this.accessTokenSupport.getAccessToken();
		return tokenValue;
	}

	/** LifecycleAware */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		ConfigRegistryConfigPropertiesHandler.getInstance().addPropertyChangeListener(this);

		updateConnectionProperties();

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(ConfigRegistryService.class, this, props);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}

		ConfigRegistryConfigPropertiesHandler.getInstance().removePropertyChangeListener(this);
	}

	/** PropertyChangeListener */
	@Override
	public void notifyEvent(PropertyChangeEvent event) {
		String eventName = event.getName();
		if (InfraConstants.CONFIG_REGISTRY__JDBC_DRIVER.equals(eventName) //
				|| InfraConstants.CONFIG_REGISTRY__JDBC_URL.equals(eventName) //
				|| InfraConstants.CONFIG_REGISTRY__JDBC_USERNAME.equals(eventName) //
				|| InfraConstants.CONFIG_REGISTRY__JDBC_PASSWORD.equals(eventName)) {
			updateConnectionProperties();
		}
	}

	protected synchronized void updateConnectionProperties() {
		ConfigRegistryConfigPropertiesHandler configPropertiesHandler = ConfigRegistryConfigPropertiesHandler.getInstance();
		String driver = configPropertiesHandler.getProperty(InfraConstants.CONFIG_REGISTRY__JDBC_DRIVER, this.initProperties);
		String url = configPropertiesHandler.getProperty(InfraConstants.CONFIG_REGISTRY__JDBC_URL, this.initProperties);
		String username = configPropertiesHandler.getProperty(InfraConstants.CONFIG_REGISTRY__JDBC_USERNAME, this.initProperties);
		String password = configPropertiesHandler.getProperty(InfraConstants.CONFIG_REGISTRY__JDBC_PASSWORD, this.initProperties);

		this.databaseProperties = DatabaseUtil.getProperties(driver, url, username, password);
	}

	/** ConnectionAware */
	@Override
	public Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	/** WebServiceAware */
	@Override
	public String getName() {
		return ConfigRegistryConfigPropertiesHandler.getInstance().getProperty(InfraConstants.CONFIG_REGISTRY__NAME, this.initProperties);
	}

	@Override
	public String getHostURL() {
		String hostURL = ConfigRegistryConfigPropertiesHandler.getInstance().getProperty(InfraConstants.CONFIG_REGISTRY__HOST_URL, this.initProperties);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = ConfigRegistryConfigPropertiesHandler.getInstance().getProperty(InfraConstants.ORBIT_HOST_URL, this.initProperties);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		return ConfigRegistryConfigPropertiesHandler.getInstance().getProperty(InfraConstants.CONFIG_REGISTRY__CONTEXT_ROOT, this.initProperties);
	}

	/** EditPoliciesAwareService */
	@Override
	public ServiceEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	protected ConfigRegistryMetadatasTableHandler getConfigRegistryMetadatasTableHandler(Connection conn) throws SQLException {
		return ConfigRegistryMetadatasTableHandler.getInstance(conn, this.databaseProperties);
	}

	/**
	 * 
	 * @param metadata
	 * @return
	 */
	protected synchronized ConfigRegistry getConfigRegistry(ConfigRegistryMetadata metadata) {
		ConfigRegistry configRegistry = new ConfigRegistryImpl(this, metadata);

		// String id = metadata.getId();
		// configRegistry = this.configRegistryMap.get(id);
		// if (configRegistry == null) {
		// configRegistry = new ConfigRegistryImpl(this, metadata);
		// this.configRegistryMap.put(id, configRegistry);
		// }

		return configRegistry;
	}

	// /**
	// *
	// * @param metadatas
	// */
	// protected synchronized void cleanupAll(List<ConfigRegistryMetadata> metadatas) {
	// if (metadatas == null) {
	// return;
	// }
	//
	// List<String> ids = new ArrayList<String>();
	// for (ConfigRegistryMetadata metadata : metadatas) {
	// ids.add(metadata.getId());
	// }
	//
	// List<String> idsToRemove = new ArrayList<String>();
	// for (Iterator<String> itor = this.configRegistryMap.keySet().iterator(); itor.hasNext();) {
	// String currId = itor.next();
	// if (!ids.contains(currId)) {
	// idsToRemove.add(currId);
	// }
	// }
	// for (String idToRemove : idsToRemove) {
	// this.configRegistryMap.remove(idToRemove);
	// }
	// }

	// /**
	// *
	// * @param id
	// * @throws ServerException
	// */
	// protected synchronized void cleanupById(String id) throws ServerException {
	// if (id == null) {
	// return;
	// }
	//
	// Connection conn = null;
	// try {
	// conn = getConnection();
	// ConfigRegistryMetadatasTableHandler tableHandler = getConfigRegistryMetadatasTableHandler(conn);
	//
	// ConfigRegistryMetadata metadata = tableHandler.getById(conn, id);
	//
	// if (metadata == null) {
	// // metadata doesn't exist or deleted
	// // - remove the ConfigRegistry
	// if (this.configRegistryMap.containsKey(id)) {
	// this.configRegistryMap.remove(id);
	// }
	//
	// } else {
	// // metadata exists
	// // - update existing ConfigRegistry's metadata (if ConfigRegistry exists)
	// ConfigRegistry configRegistry = this.configRegistryMap.get(id);
	// if (configRegistry != null) {
	// configRegistry.setMetadata(metadata);
	// }
	// }
	//
	// } catch (SQLException e) {
	// handleException(e);
	// } finally {
	// DatabaseUtil.closeQuietly(conn, true);
	// }
	// }

	// /**
	// *
	// * @param name
	// * @throws ServerException
	// */
	// protected synchronized void cleanupByName(String name) throws ServerException {
	// if (name == null) {
	// return;
	// }
	//
	// Connection conn = null;
	// try {
	// conn = getConnection();
	// ConfigRegistryMetadatasTableHandler tableHandler = getConfigRegistryMetadatasTableHandler(conn);
	//
	// ConfigRegistryMetadata metadata = tableHandler.getByName(conn, name);
	//
	// if (metadata == null) {
	// // metadata doesn't exist or deleted
	// // - remove the ConfigRegistry (if ConfigRegistry exists)
	// if (this.configRegistryMap.containsKey(name)) {
	// this.configRegistryMap.remove(name);
	// }
	//
	// } else {
	// // metadata exists
	// // - update existing ConfigRegistry's metadata (if ConfigRegistry exists)
	// String id = metadata.getId();
	// ConfigRegistry configRegistry = this.configRegistryMap.get(id);
	// if (configRegistry != null) {
	// configRegistry.setMetadata(metadata);
	// }
	// }
	//
	// } catch (SQLException e) {
	// handleException(e);
	// } finally {
	// DatabaseUtil.closeQuietly(conn, true);
	// }
	// }

	@Override
	public ConfigRegistry[] getConfigRegistries() throws ServerException {
		List<ConfigRegistry> configRegistries = new ArrayList<ConfigRegistry>();
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryMetadatasTableHandler tableHandler = getConfigRegistryMetadatasTableHandler(conn);

			List<ConfigRegistryMetadata> metadatas = tableHandler.getList(conn);

			if (metadatas != null) {
				for (ConfigRegistryMetadata metadata : metadatas) {
					ConfigRegistry configRegistry = getConfigRegistry(metadata);
					if (configRegistry != null) {
						configRegistries.add(configRegistry);
					}
				}
				// cleanupAll(metadatas);
			}

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return configRegistries.toArray(new ConfigRegistry[configRegistries.size()]);
	}

	@Override
	public ConfigRegistry[] getConfigRegistries(String type) throws ServerException {
		List<ConfigRegistry> configRegistries = new ArrayList<ConfigRegistry>();
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryMetadatasTableHandler tableHandler = getConfigRegistryMetadatasTableHandler(conn);

			List<ConfigRegistryMetadata> metadatas = tableHandler.getList(conn, type);

			if (metadatas != null) {
				for (ConfigRegistryMetadata metadata : metadatas) {
					ConfigRegistry configRegistry = getConfigRegistry(metadata);
					if (configRegistry != null) {
						configRegistries.add(configRegistry);
					}
				}
				// cleanupAll(metadatas);
			}

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return configRegistries.toArray(new ConfigRegistry[configRegistries.size()]);
	}

	@Override
	public ConfigRegistry getConfigRegistryById(String id) throws ServerException {
		ConfigRegistry configRegistry = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryMetadatasTableHandler tableHandler = getConfigRegistryMetadatasTableHandler(conn);

			ConfigRegistryMetadata metadata = tableHandler.getById(conn, id);
			if (metadata != null) {
				configRegistry = getConfigRegistry(metadata);
			}

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return configRegistry;
	}

	@Override
	public ConfigRegistry getConfigRegistryByName(String fullName) throws ServerException {
		ConfigRegistry configRegistry = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryMetadatasTableHandler tableHandler = getConfigRegistryMetadatasTableHandler(conn);

			ConfigRegistryMetadata metadata = tableHandler.getByName(conn, fullName);
			if (metadata != null) {
				configRegistry = getConfigRegistry(metadata);
			}

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return configRegistry;
	}

	@Override
	public boolean configRegistryExistsById(String id) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryMetadatasTableHandler tableHandler = getConfigRegistryMetadatasTableHandler(conn);

			exists = tableHandler.existsById(conn, id);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public boolean configRegistryExistsByName(String fullName) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryMetadatasTableHandler tableHandler = getConfigRegistryMetadatasTableHandler(conn);

			exists = tableHandler.existsByName(conn, fullName);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public ConfigRegistry createConfigRegistry(String type, String fullName, Map<String, Object> properties) throws ServerException {
		ConfigRegistry configRegistry = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryMetadatasTableHandler tableHandler = getConfigRegistryMetadatasTableHandler(conn);

			if (fullName == null || fullName.isEmpty()) {
				throw new ServerException("500", "Name is empty.");
			}
			if (configRegistryExistsByName(fullName)) {
				throw new ServerException("500", "Name '" + fullName + "' already exists.");
			}
			String id = generateConfigRegistryId();
			if (configRegistryExistsById(id)) {
				throw new ServerException("500", "Id '" + id + "'already exists.");
			}
			if (properties == null) {
				properties = new HashMap<String, Object>();
			}

			ConfigRegistryMetadata metadata = tableHandler.create(conn, id, type, fullName, properties);
			if (metadata == null) {
				throw new ServerException("500", "ConfigRegistry metadata cannot be created.");
			}

			configRegistry = getConfigRegistry(metadata);

		} catch (SQLException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return configRegistry;
	}

	public String generateConfigRegistryId() {
		String uuid = UUIDUtil.generateBase64EncodedUUID();
		return uuid;
	}

	@Override
	public boolean updateConfigRegistryType(String id, String type) throws ServerException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryMetadatasTableHandler tableHandler = getConfigRegistryMetadatasTableHandler(conn);

			isUpdated = tableHandler.updateType(conn, id, type);

			// if (isUpdated) {
			// cleanupById(id);
			// }

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateConfigRegistryName(String id, String fullName) throws ServerException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryMetadatasTableHandler tableHandler = getConfigRegistryMetadatasTableHandler(conn);

			isUpdated = tableHandler.updateName(conn, id, fullName);

			// if (isUpdated) {
			// cleanupById(id);
			// }

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateConfigRegistryProperties(String id, Map<String, Object> properties) throws ServerException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryMetadatasTableHandler tableHandler = getConfigRegistryMetadatasTableHandler(conn);

			isUpdated = tableHandler.updateProperties(conn, id, properties);

			// if (isUpdated) {
			// cleanupById(id);
			// }

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isUpdated;
	}

	@Override
	public boolean deleteConfigRegistryById(String id) throws ServerException {
		boolean isDeleted = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryMetadatasTableHandler tableHandler = getConfigRegistryMetadatasTableHandler(conn);

			isDeleted = tableHandler.deleteById(conn, id);

			// if (isDeleted) {
			// cleanupById(id);
			// }

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isDeleted;
	}

	@Override
	public boolean deleteConfigRegistryByName(String fullName) throws ServerException {
		boolean isDeleted = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryMetadatasTableHandler tableHandler = getConfigRegistryMetadatasTableHandler(conn);

			isDeleted = tableHandler.deleteByName(conn, fullName);

			// if (isDeleted) {
			// cleanupByName(fullName);
			// }

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isDeleted;
	}

	/**
	 * 
	 * @param e
	 * @throws ServerException
	 */
	protected void handleException(Exception e) throws ServerException {
		throw new ServerException(StatusDTO.RESP_500, e.getMessage(), e);
	}

}

// protected Map<Object, Object> properties = new HashMap<Object, Object>();
// @Override
// public void start(BundleContext bundleContext) throws Exception {
// // Map<Object, Object> properties = new Hashtable<Object, Object>();
// // if (this.initProperties != null) {
// // properties.putAll(this.initProperties);
// // }
// // PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_INDEX_SERVICE_URL);
// // PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_HOST_URL);
// // PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.CONFIG_REGISTRY__ID);
// // PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.CONFIG_REGISTRY__NAME);
// // PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.CONFIG_REGISTRY__HOST_URL);
// // PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.CONFIG_REGISTRY__CONTEXT_ROOT);
// // PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.CONFIG_REGISTRY__JDBC_DRIVER);
// // PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.CONFIG_REGISTRY__JDBC_URL);
// // PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.CONFIG_REGISTRY__JDBC_USERNAME);
// // PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.CONFIG_REGISTRY__JDBC_PASSWORD);
// // updateProperties(properties);
// // initialize();
//
// this.databaseProperties = getConnectionProperties();
//
// Hashtable<String, Object> props = new Hashtable<String, Object>();
// this.serviceRegistry = bundleContext.registerService(ConfigRegistryService.class, this, props);
// }

// /**
// *
// * @param props
// * @return
// */
// protected synchronized Properties getConnectionProperties(Map<Object, Object> props) {
// String driver = (String) this.properties.get(InfraConstants.CONFIG_REGISTRY__JDBC_DRIVER);
// String url = (String) this.properties.get(InfraConstants.CONFIG_REGISTRY__JDBC_URL);
// String username = (String) this.properties.get(InfraConstants.CONFIG_REGISTRY__JDBC_USERNAME);
// String password = (String) this.properties.get(InfraConstants.CONFIG_REGISTRY__JDBC_PASSWORD);
// return DatabaseUtil.getProperties(driver, url, username, password);
// }

// @Override
// public Map<Object, Object> getProperties() {
// return this.properties;
// }

// /**
// *
// * @param configProps
// */
// public synchronized void updateProperties(Map<Object, Object> configProps) {
// if (configProps == null) {
// configProps = new HashMap<Object, Object>();
// }
//
// String indexServiceUrl = (String) configProps.get(InfraConstants.ORBIT_INDEX_SERVICE_URL);
// String globalHostURL = (String) configProps.get(InfraConstants.ORBIT_HOST_URL);
// String id = (String) configProps.get(InfraConstants.CONFIG_REGISTRY__ID);
// String name = (String) configProps.get(InfraConstants.CONFIG_REGISTRY__NAME);
// String hostURL = (String) configProps.get(InfraConstants.CONFIG_REGISTRY__HOST_URL);
// String contextRoot = (String) configProps.get(InfraConstants.CONFIG_REGISTRY__CONTEXT_ROOT);
// String jdbcDriver = (String) configProps.get(InfraConstants.CONFIG_REGISTRY__JDBC_DRIVER);
// String jdbcURL = (String) configProps.get(InfraConstants.CONFIG_REGISTRY__JDBC_URL);
// String jdbcUsername = (String) configProps.get(InfraConstants.CONFIG_REGISTRY__JDBC_USERNAME);
// String jdbcPassword = (String) configProps.get(InfraConstants.CONFIG_REGISTRY__JDBC_PASSWORD);
//
// boolean printProps = false;
// if (printProps) {
// System.out.println();
// System.out.println("Config properties:");
// System.out.println("-----------------------------------------------------");
// System.out.println(InfraConstants.ORBIT_INDEX_SERVICE_URL + " = " + indexServiceUrl);
// System.out.println(InfraConstants.ORBIT_HOST_URL + " = " + globalHostURL);
// // System.out.println(InfraConstants.CONFIG_REGISTRY__ID + " = " + id);
// System.out.println(InfraConstants.CONFIG_REGISTRY__NAME + " = " + name);
// System.out.println(InfraConstants.CONFIG_REGISTRY__HOST_URL + " = " + hostURL);
// System.out.println(InfraConstants.CONFIG_REGISTRY__CONTEXT_ROOT + " = " + contextRoot);
// System.out.println(InfraConstants.CONFIG_REGISTRY__JDBC_DRIVER + " = " + jdbcDriver);
// System.out.println(InfraConstants.CONFIG_REGISTRY__JDBC_URL + " = " + jdbcURL);
// System.out.println(InfraConstants.CONFIG_REGISTRY__JDBC_USERNAME + " = " + jdbcUsername);
// System.out.println(InfraConstants.CONFIG_REGISTRY__JDBC_PASSWORD + " = " + jdbcPassword);
// System.out.println("-----------------------------------------------------");
// System.out.println();
// }
//
// this.properties = configProps;
// this.databaseProperties = getConnectionProperties(this.properties);
// }

// @Override
// public String getName() {
// return (String) this.properties.get(InfraConstants.CONFIG_REGISTRY__NAME);
// }
//
// @Override
// public String getHostURL() {
// String hostURL = (String) this.properties.get(InfraConstants.CONFIG_REGISTRY__HOST_URL);
// if (hostURL != null) {
// return hostURL;
// }
// String globalHostURL = (String) this.properties.get(InfraConstants.ORBIT_HOST_URL);
// if (globalHostURL != null) {
// return globalHostURL;
// }
// return null;
// }
//
// @Override
// public String getContextRoot() {
// return (String) this.properties.get(InfraConstants.CONFIG_REGISTRY__CONTEXT_ROOT);
// }
