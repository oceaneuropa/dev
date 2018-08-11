package org.orbit.infra.runtime.extensionregistry.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.infra.model.extensionregistry.ExtensionItemVO;
import org.orbit.infra.runtime.InfraConstants;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtensionRegistryServiceImpl implements ExtensionRegistryService, LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(ExtensionRegistryServiceImpl.class);

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;

	/**
	 * 
	 * @param initProperties
	 */
	public ExtensionRegistryServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
	}

	/**
	 * Start service.
	 * 
	 */
	@Override
	public void start(BundleContext bundleContext) {
		LOG.debug("start()");

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}

		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_EXTENSION_REGISTRY_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_EXTENSION_REGISTRY_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_EXTENSION_REGISTRY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_PASSWORD);

		update(properties);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(ExtensionRegistryService.class, this, props);
	}

	/**
	 * Stop service.
	 * 
	 */
	@Override
	public void stop(BundleContext bundleContext) {
		LOG.debug("stop()");

		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}
	}

	/**
	 * 
	 * @param props
	 */
	public synchronized void update(Map<Object, Object> properties) {
		LOG.debug("update()");
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}

		String globalHostURL = (String) properties.get(InfraConstants.ORBIT_HOST_URL);
		String name = (String) properties.get(InfraConstants.COMPONENT_EXTENSION_REGISTRY_NAME);
		String hostURL = (String) properties.get(InfraConstants.COMPONENT_EXTENSION_REGISTRY_HOST_URL);
		String contextRoot = (String) properties.get(InfraConstants.COMPONENT_EXTENSION_REGISTRY_CONTEXT_ROOT);
		String jdbcDriver = (String) properties.get(InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_DRIVER);
		String jdbcURL = (String) properties.get(InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_URL);
		String jdbcUsername = (String) properties.get(InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_USERNAME);
		String jdbcPassword = (String) properties.get(InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_PASSWORD);

		LOG.debug("Properties:");
		LOG.debug("-----------------------------------------------------------------------------");
		LOG.debug(InfraConstants.ORBIT_HOST_URL + " = " + globalHostURL);
		LOG.debug(InfraConstants.COMPONENT_EXTENSION_REGISTRY_NAME + " = " + name);
		LOG.debug(InfraConstants.COMPONENT_EXTENSION_REGISTRY_HOST_URL + " = " + hostURL);
		LOG.debug(InfraConstants.COMPONENT_EXTENSION_REGISTRY_CONTEXT_ROOT + " = " + contextRoot);
		LOG.debug(InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_DRIVER + " = " + jdbcDriver);
		LOG.debug(InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_URL + " = " + jdbcURL);
		LOG.debug(InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_USERNAME + " = " + jdbcUsername);
		LOG.debug(InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_PASSWORD + " = " + jdbcPassword);
		LOG.debug("-----------------------------------------------------------------------------");

		this.properties = properties;
		this.databaseProperties = getConnectionProperties(this.properties);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getProperty(Object key, Class<T> valueClass) {
		// Config properties from bundle context or from system/env properties takes precedence over properties defined in config.ini file.
		Object object = this.properties.get(key);
		if (object != null && valueClass.isAssignableFrom(object.getClass())) {
			return (T) object;
		}
		return null;
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	protected synchronized Properties getConnectionProperties(Map<Object, Object> props) {
		String driver = getProperty(InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_DRIVER, String.class);
		String url = getProperty(InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_URL, String.class);
		String username = getProperty(InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_USERNAME, String.class);
		String password = getProperty(InfraConstants.COMPONENT_EXTENSION_REGISTRY_JDBC_PASSWORD, String.class);
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	protected Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	@Override
	public String getName() {
		String name = getProperty(InfraConstants.COMPONENT_EXTENSION_REGISTRY_NAME, String.class);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = getProperty(InfraConstants.COMPONENT_EXTENSION_REGISTRY_HOST_URL, String.class);
		if (hostURL != null) {
			return hostURL;
		}

		// default global config
		String globalHostURL = (String) this.properties.get(InfraConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}

		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = getProperty(InfraConstants.COMPONENT_EXTENSION_REGISTRY_CONTEXT_ROOT, String.class);
		return contextRoot;
	}

	/**
	 * 
	 * @param itemVO
	 * @return
	 */
	protected ExtensionItem toItem(ExtensionItemVO itemVO) {
		Integer id = itemVO.getId();
		String platformId = itemVO.getPlatformId();
		String typeId = itemVO.getTypeId();
		String extensionId = itemVO.getExtensionId();
		String name = itemVO.getName();
		String description = itemVO.getDescription();
		String propertiesString = itemVO.getPropertiesString();
		Date createTime = itemVO.getCreateTime();
		Date lastUpdateTime = itemVO.getLastUpdateTime();

		Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

		ExtensionItem indexItem = new ExtensionItem(id, platformId, typeId, extensionId, name, description, properties, createTime, lastUpdateTime);
		return indexItem;
	}

	/**
	 * 
	 * @param e
	 * @throws ServerException
	 */
	protected void handleSQLException(SQLException e) throws ServerException {
		throw new ServerException(StatusDTO.RESP_500, e.getMessage(), e);
	}

	@Override
	public List<ExtensionItem> getExtensionItems(String platformId) throws ServerException {
		List<ExtensionItem> items = new ArrayList<ExtensionItem>();
		Connection conn = null;
		try {
			conn = getConnection();
			ExtensionItemsTableHandler tableHandler = ExtensionItemsTableHandler.getInstance(conn, platformId);
			List<ExtensionItemVO> itemVOs = tableHandler.getExtensionItems(conn);
			if (itemVOs != null) {
				for (ExtensionItemVO itemVO : itemVOs) {
					ExtensionItem item = toItem(itemVO);
					items.add(item);
				}
			}
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return items;
	}

	@Override
	public List<ExtensionItem> getExtensionItems(String platformId, String typeId) throws ServerException {
		List<ExtensionItem> items = new ArrayList<ExtensionItem>();
		Connection conn = null;
		try {
			conn = getConnection();
			ExtensionItemsTableHandler tableHandler = ExtensionItemsTableHandler.getInstance(conn, platformId);
			List<ExtensionItemVO> itemVOs = tableHandler.getExtensionItems(conn, typeId);
			if (itemVOs != null) {
				for (ExtensionItemVO itemVO : itemVOs) {
					ExtensionItem item = toItem(itemVO);
					items.add(item);
				}
			}
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return items;
	}

	@Override
	public ExtensionItem getExtensionItem(String platformId, String typeId, String extensionId) throws ServerException {
		ExtensionItem item = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ExtensionItemsTableHandler tableHandler = ExtensionItemsTableHandler.getInstance(conn, platformId);
			ExtensionItemVO itemVO = tableHandler.getExtensionItem(conn, typeId, extensionId);
			if (itemVO != null) {
				item = toItem(itemVO);
			}
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return item;
	}

	@Override
	public ExtensionItem addExtensionItem(String platformId, String typeId, String extensionId, String name, String description, Map<String, Object> properties) throws ServerException {
		ExtensionItem item = null;
		Connection conn = null;
		try {
			conn = getConnection();

			String propertiesString = JSONUtil.toJsonString(properties);
			Date createTime = new Date();
			Date lastUpdateTime = createTime;

			ExtensionItemsTableHandler tableHandler = ExtensionItemsTableHandler.getInstance(conn, platformId);
			ExtensionItemVO itemVO = tableHandler.insert(conn, typeId, extensionId, name, description, propertiesString, createTime, lastUpdateTime);
			if (itemVO != null) {
				item = toItem(itemVO);
			}
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return item;
	}

	@Override
	public boolean updateExtensionItem(String platformId, String typeId, String extensionId, String newTypeId, String newExtensionId, String newName, String newDescription, Map<String, Object> properties) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();

			ExtensionItemsTableHandler tableHandler = ExtensionItemsTableHandler.getInstance(conn, platformId);
			ExtensionItemVO itemVO = tableHandler.getExtensionItem(conn, typeId, extensionId);
			if (itemVO != null) {
				Integer id = itemVO.getId();
				String name = itemVO.getName();
				String description = itemVO.getDescription();

				Date lastUpdateTime = new Date();

				if (newTypeId != null && !newTypeId.equals(typeId)) {
					tableHandler.updateTypeId(conn, id, newTypeId, lastUpdateTime);
				}
				if (newExtensionId != null && !newExtensionId.equals(extensionId)) {
					tableHandler.updateExtensionId(conn, id, newExtensionId, lastUpdateTime);
				}
				if (newName != null && !newName.equals(name)) {
					tableHandler.updateName(conn, id, name, lastUpdateTime);
				}
				if (newDescription != null && !newDescription.equals(description)) {
					tableHandler.updateDescription(conn, id, description, lastUpdateTime);
				}
				if (properties != null && !properties.isEmpty()) {
					String propertiesString = JSONUtil.toJsonString(properties);
					tableHandler.updateProperties(conn, typeId, extensionId, propertiesString, lastUpdateTime);
				}
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	/**
	 * 
	 * @param platformId
	 * @return
	 * @throws ServerException
	 */
	@Override
	public boolean removeExtensionItems(String platformId) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();

			ExtensionItemsTableHandler tableHandler = ExtensionItemsTableHandler.getInstance(conn, platformId);
			succeed = tableHandler.deleteAll(conn);

			ExtensionItemsTableHandler.dispose(conn, platformId);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean removeExtensionItem(String platformId, String typeId, String extensionId) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ExtensionItemsTableHandler tableHandler = ExtensionItemsTableHandler.getInstance(conn, platformId);
			succeed = tableHandler.delete(conn, typeId, extensionId);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public Map<String, Object> getProperties(String platformId, String typeId, String extensionId) throws ServerException {
		Map<String, Object> props = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ExtensionItemsTableHandler tableHandler = ExtensionItemsTableHandler.getInstance(conn, platformId);
			ExtensionItemVO itemVO = tableHandler.getExtensionItem(conn, typeId, extensionId);
			if (itemVO != null) {
				String propertiesString = itemVO.getPropertiesString();
				if (propertiesString != null) {
					props = JSONUtil.toProperties(propertiesString, true);
				}
			}
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (props == null) {
			props = new HashMap<String, Object>();
		}
		return props;
	}

	@Override
	public boolean setProperties(String platformId, String typeId, String extensionId, Map<String, Object> properties) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();

			String propertiesString = JSONUtil.toJsonString(properties);
			Date lastUpdateTime = new Date();

			ExtensionItemsTableHandler tableHandler = ExtensionItemsTableHandler.getInstance(conn, platformId);
			succeed = tableHandler.updateProperties(conn, typeId, extensionId, propertiesString, lastUpdateTime);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean removeProperties(String platformId, String typeId, String extensionId, List<String> propNames) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ExtensionItemsTableHandler tableHandler = ExtensionItemsTableHandler.getInstance(conn, platformId);

			ExtensionItemVO itemVO = tableHandler.getExtensionItem(conn, typeId, extensionId);
			if (itemVO != null) {
				String oldPropertiesString = itemVO.getPropertiesString();
				if (oldPropertiesString != null) {
					Map<String, Object> newProperties = new HashMap<String, Object>();

					Map<String, Object> oldProperties = JSONUtil.toProperties(oldPropertiesString, true);
					newProperties.putAll(oldProperties);

					for (String propName : propNames) {
						newProperties.remove(propName);
					}

					String newPropertiesString = JSONUtil.toJsonString(newProperties);
					Date lastUpdateTime = new Date();
					succeed = tableHandler.updateProperties(conn, typeId, extensionId, newPropertiesString, lastUpdateTime);
				}
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

}
