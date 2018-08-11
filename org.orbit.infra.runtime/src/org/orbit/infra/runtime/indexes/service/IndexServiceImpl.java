package org.orbit.infra.runtime.indexes.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.model.indexes.IndexItemVO;
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

public class IndexServiceImpl implements IndexService, LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(IndexServiceImpl.class);

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;

	/**
	 * 
	 * @param initProperties
	 */
	public IndexServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
	}

	/**
	 * Start IndexService
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
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD);

		update(properties);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(IndexService.class, this, props);
	}

	/**
	 * Stop IndexService
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
		// System.out.println(getClass().getSimpleName() + ".update()");

		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}

		String globalHostURL = (String) properties.get(InfraConstants.ORBIT_HOST_URL);
		String name = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_NAME);
		String hostURL = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_HOST_URL);
		String contextRoot = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT);
		String jdbcDriver = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER);
		String jdbcURL = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_URL);
		String jdbcUsername = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME);
		String jdbcPassword = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD);

		LOG.debug("Properties:");
		LOG.debug("-----------------------------------------------------------------------------");
		LOG.debug(InfraConstants.ORBIT_HOST_URL + " = " + globalHostURL);
		LOG.debug(InfraConstants.COMPONENT_INDEX_SERVICE_NAME + " = " + name);
		LOG.debug(InfraConstants.COMPONENT_INDEX_SERVICE_HOST_URL + " = " + hostURL);
		LOG.debug(InfraConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT + " = " + contextRoot);
		LOG.debug(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER + " = " + jdbcDriver);
		LOG.debug(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_URL + " = " + jdbcURL);
		LOG.debug(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME + " = " + jdbcUsername);
		LOG.debug(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD + " = " + jdbcPassword);
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
		String driver = getProperty(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER, String.class);
		String url = getProperty(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_URL, String.class);
		String username = getProperty(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME, String.class);
		String password = getProperty(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD, String.class);
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	protected Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	@Override
	public String getName() {
		String name = getProperty(InfraConstants.COMPONENT_INDEX_SERVICE_NAME, String.class);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = getProperty(InfraConstants.COMPONENT_INDEX_SERVICE_HOST_URL, String.class);
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
		String contextRoot = getProperty(InfraConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT, String.class);
		return contextRoot;
	}

	// /**
	// *
	// * @param e
	// * @throws IndexServiceException
	// */
	// protected void handleSQLException(SQLException e) throws IndexServiceException {
	// e.printStackTrace();
	// throw new IndexServiceException(StatusDTO.RESP_500, e.getMessage(), e);
	// }

	/**
	 * 
	 * @param e
	 * @throws ServerException
	 */
	protected void handleSQLException(SQLException e) throws ServerException {
		e.printStackTrace();
		throw new ServerException(StatusDTO.RESP_500, e.getMessage(), e);
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId) throws ServerException {
		LOG.debug("getIndexItems(String)");
		LOG.debug("    indexProviderId = " + indexProviderId);

		List<IndexItem> indexItems = new ArrayList<IndexItem>();
		Connection conn = null;
		try {
			conn = getConnection();
			IndexItemsTableHandler tableHandler = IndexItemsTableHandler.getInstance(conn, indexProviderId);
			List<IndexItemVO> indexItemVOs = tableHandler.getIndexItems(conn);
			LOG.debug("---------------------------------------------------------------------------");
			if (indexItemVOs != null) {
				for (IndexItemVO indexItemVO : indexItemVOs) {
					LOG.debug(indexItemVO.toString());
					IndexItem indexItem = IndexServiceDatabaseHelper.INSTANCE.toIndexItem(indexItemVO);
					indexItems.add(indexItem);
				}
			}
			LOG.debug("---------------------------------------------------------------------------");

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return indexItems;
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId, String type) throws ServerException {
		LOG.debug("getIndexItems(String, String)");
		LOG.debug("    indexProviderId = " + indexProviderId);
		LOG.debug("    type = " + type);

		List<IndexItem> indexItems = new ArrayList<IndexItem>();
		Connection conn = null;
		try {
			conn = getConnection();
			IndexItemsTableHandler tableHandler = IndexItemsTableHandler.getInstance(conn, indexProviderId);
			List<IndexItemVO> indexItemVOs = tableHandler.getIndexItems(conn, type);
			LOG.debug("---------------------------------------------------------------------------");
			if (indexItemVOs != null) {
				for (IndexItemVO indexItemVO : indexItemVOs) {
					LOG.debug(indexItemVO.toString());
					IndexItem indexItem = IndexServiceDatabaseHelper.INSTANCE.toIndexItem(indexItemVO);
					indexItems.add(indexItem);
				}
			}
			LOG.debug("---------------------------------------------------------------------------");

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return indexItems;
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, String type, String name) throws ServerException {
		LOG.debug("getIndexItem(String, String, String)");
		LOG.debug("    indexProviderId = " + indexProviderId);
		LOG.debug("    type = " + type);
		LOG.debug("    name = " + name);

		Connection conn = null;
		try {
			conn = getConnection();
			IndexItemsTableHandler tableHandler = IndexItemsTableHandler.getInstance(conn, indexProviderId);
			IndexItemVO indexItemVO = tableHandler.getIndexItem(conn, type, name);
			if (indexItemVO != null) {
				LOG.debug("---------------------------------------------------------------------------");
				LOG.debug(indexItemVO.toString());
				LOG.debug("---------------------------------------------------------------------------");

				IndexItem indexItem = IndexServiceDatabaseHelper.INSTANCE.toIndexItem(indexItemVO);

				return indexItem;
			}
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, Integer indexItemId) throws ServerException {
		LOG.debug("getIndexItem(String, Integer)");
		LOG.debug("    indexProviderId = " + indexProviderId);
		LOG.debug("    indexItemId = " + indexItemId);

		Connection conn = null;
		try {
			conn = getConnection();
			IndexItemsTableHandler tableHandler = IndexItemsTableHandler.getInstance(conn, indexProviderId);
			IndexItemVO indexItemVO = tableHandler.getIndexItem(conn, indexItemId);
			if (indexItemVO != null) {
				LOG.debug("---------------------------------------------------------------------------");
				LOG.debug(indexItemVO.toString());
				LOG.debug("---------------------------------------------------------------------------");

				IndexItem indexItem = IndexServiceDatabaseHelper.INSTANCE.toIndexItem(indexItemVO);
				return indexItem;
			}
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws ServerException {
		LOG.debug("addIndexItem(String, String, String, Map<String, Object>)");
		LOG.debug("    indexProviderId = " + indexProviderId);
		LOG.debug("    type = " + type);
		LOG.debug("    name = " + name);
		if (properties == null) {
			LOG.debug("    properties: n/a");
		} else {
			LOG.debug("    properties:");
			for (Iterator<String> itor = properties.keySet().iterator(); itor.hasNext();) {
				String propName = itor.next();
				Object propValue = properties.get(propName);
				LOG.debug("        " + propName + " = " + propValue);
			}
		}

		Connection conn = null;
		try {
			conn = getConnection();
			IndexItemsTableHandler tableHandler = IndexItemsTableHandler.getInstance(conn, indexProviderId);

			String propertiesString = JSONUtil.toJsonString(properties);
			Date createTime = new Date();
			Date lastUpdateTime = createTime;

			IndexItemVO newIndexItemVO = tableHandler.insert(conn, type, name, propertiesString, createTime, lastUpdateTime);
			if (newIndexItemVO != null) {
				LOG.debug("---------------------------------------------------------------------------");
				LOG.debug(newIndexItemVO.toString());
				LOG.debug("---------------------------------------------------------------------------");

				IndexItem indexItem = IndexServiceDatabaseHelper.INSTANCE.toIndexItem(newIndexItemVO);
				return indexItem;
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws ServerException {
		LOG.debug("removeIndexItem(String, Integer)");
		LOG.debug("    indexProviderId = " + indexProviderId);
		LOG.debug("    indexItemId = " + indexItemId);

		Connection conn = null;
		try {
			conn = getConnection();
			IndexItemsTableHandler tableHandler = IndexItemsTableHandler.getInstance(conn, indexProviderId);
			return tableHandler.delete(conn, indexItemId);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean hasProperty(String indexProviderId, Integer indexItemId, String propName) throws ServerException {
		LOG.debug("hasProperty(String, Integer, String)");
		LOG.debug("    indexProviderId = " + indexProviderId);
		LOG.debug("    indexItemId = " + indexItemId);
		LOG.debug("    propName = " + propName);

		Connection conn = null;
		try {
			conn = getConnection();
			IndexItemsTableHandler tableHandler = IndexItemsTableHandler.getInstance(conn, indexProviderId);

			IndexItemVO indexItemVO = tableHandler.getIndexItem(conn, indexItemId);
			if (indexItemVO != null) {
				String propertiesString = indexItemVO.getPropertiesString();
				if (propertiesString != null) {
					Map<String, Object> props = JSONUtil.toProperties(propertiesString, true);
					if (props != null) {
						return (props.containsKey(propName)) ? true : false;
					}
				}
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public Map<String, Object> getProperties(String indexProviderId, Integer indexItemId) throws ServerException {
		LOG.debug("getProperties(String, Integer)");
		LOG.debug("    indexProviderId = " + indexProviderId);
		LOG.debug("    indexItemId = " + indexItemId);

		Connection conn = null;
		try {
			conn = getConnection();
			IndexItemsTableHandler tableHandler = IndexItemsTableHandler.getInstance(conn, indexProviderId);

			IndexItemVO indexItemVO = tableHandler.getIndexItem(conn, indexItemId);
			if (indexItemVO != null) {
				String propertiesString = indexItemVO.getPropertiesString();
				if (propertiesString != null) {
					Map<String, Object> props = JSONUtil.toProperties(propertiesString, true);
					return props;
				}
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public Object getProperty(String indexProviderId, Integer indexItemId, String propName) throws ServerException {
		LOG.debug("getProperty(String, Integer, String)");
		LOG.debug("    indexProviderId = " + indexProviderId);
		LOG.debug("    indexItemId = " + indexItemId);
		LOG.debug("    propName = " + propName);

		Connection conn = null;
		try {
			conn = getConnection();
			IndexItemsTableHandler tableHandler = IndexItemsTableHandler.getInstance(conn, indexProviderId);

			IndexItemVO indexItemVO = tableHandler.getIndexItem(conn, indexItemId);
			if (propName != null && indexItemVO != null) {
				String propertiesString = indexItemVO.getPropertiesString();
				if (propertiesString != null) {
					Map<String, Object> props = JSONUtil.toProperties(propertiesString, true);
					if (props != null && props.containsKey(propName)) {
						return props.get(propName);
					}
				}
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

	@Override
	public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws ServerException {
		LOG.debug("setProperties(String, Map<String, Object>)");
		LOG.debug("    indexProviderId = " + indexProviderId);
		if (properties == null) {
			LOG.debug("    properties: n/a");
		} else {
			LOG.debug("    properties:");
			for (Iterator<String> itor = properties.keySet().iterator(); itor.hasNext();) {
				String propName = itor.next();
				Object propValue = properties.get(propName);
				LOG.debug("        " + propName + " = " + propValue);
			}
		}

		Connection conn = null;
		try {
			conn = getConnection();
			IndexItemsTableHandler tableHandler = IndexItemsTableHandler.getInstance(conn, indexProviderId);

			IndexItemVO indexItemVO = tableHandler.getIndexItem(conn, indexItemId);
			if (properties != null && indexItemVO != null) {
				Map<String, Object> newProperties = new HashMap<String, Object>();

				Map<String, Object> oldProperties = null;
				String oldPropertiesString = indexItemVO.getPropertiesString();
				if (oldPropertiesString != null) {
					oldProperties = JSONUtil.toProperties(oldPropertiesString, true);
				}
				if (oldProperties != null) {
					newProperties.putAll(oldProperties);
				}
				newProperties.putAll(properties);

				String newPropertiesString = JSONUtil.toJsonString(newProperties);
				Date lastUpdateTime = new Date();
				return tableHandler.updateProperties(conn, indexItemId, newPropertiesString, lastUpdateTime);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean removeProperty(String indexProviderId, Integer indexItemId, List<String> propNames) throws ServerException {
		LOG.debug("removeProperty(String, Integer, List<String>)");
		LOG.debug("    indexProviderId = " + indexProviderId);
		LOG.debug("    indexItemId = " + indexItemId);
		if (properties == null) {
			LOG.debug("    propNames: n/a");
		} else {
			LOG.debug("    propNames:");
			for (String propName : propNames) {
				LOG.debug("        propName = " + propName);
			}
		}

		Connection conn = null;
		try {
			conn = getConnection();
			IndexItemsTableHandler tableHandler = IndexItemsTableHandler.getInstance(conn, indexProviderId);

			IndexItemVO indexItemVO = tableHandler.getIndexItem(conn, indexItemId);
			if (propNames != null && !propNames.isEmpty() && indexItemVO != null) {
				String oldPropertiesString = indexItemVO.getPropertiesString();
				if (oldPropertiesString != null) {
					Map<String, Object> newProperties = new HashMap<String, Object>();

					Map<String, Object> oldProperties = JSONUtil.toProperties(oldPropertiesString, true);
					newProperties.putAll(oldProperties);

					for (String propName : propNames) {
						newProperties.remove(propName);
					}

					String newPropertiesString = JSONUtil.toJsonString(newProperties);
					Date lastUpdateTime = new Date();
					return tableHandler.updateProperties(conn, indexItemId, newPropertiesString, lastUpdateTime);
				}
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

}
