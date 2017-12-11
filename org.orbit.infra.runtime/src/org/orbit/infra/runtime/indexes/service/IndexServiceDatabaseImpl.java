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
import org.orbit.infra.model.indexes.IndexServiceException;
import org.orbit.infra.runtime.OrbitConstants;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexServiceDatabaseImpl implements IndexService {

	protected static Logger LOG = LoggerFactory.getLogger(IndexServiceDatabaseImpl.class);

	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;

	public IndexServiceDatabaseImpl() {
	}

	/**
	 * Start IndexService
	 * 
	 */
	public void start(BundleContext bundleContext) {
		LOG.info("start()");

		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_INDEX_SERVICE_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_INDEX_SERVICE_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD);

		update(configProps);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(IndexService.class, this, props);
	}

	/**
	 * Stop IndexService
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

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
		System.out.println(getClass().getSimpleName() + ".update()");

		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}

		String globalHostURL = (String) properties.get(OrbitConstants.ORBIT_HOST_URL);
		String name = (String) properties.get(OrbitConstants.COMPONENT_INDEX_SERVICE_NAME);
		String hostURL = (String) properties.get(OrbitConstants.COMPONENT_INDEX_SERVICE_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT);
		String jdbcDriver = (String) properties.get(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER);
		String jdbcURL = (String) properties.get(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_URL);
		String jdbcUsername = (String) properties.get(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME);
		String jdbcPassword = (String) properties.get(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD);

		LOG.info(OrbitConstants.ORBIT_HOST_URL + " = " + globalHostURL);
		LOG.info(OrbitConstants.COMPONENT_INDEX_SERVICE_NAME + " = " + name);
		LOG.info(OrbitConstants.COMPONENT_INDEX_SERVICE_HOST_URL + " = " + hostURL);
		LOG.info(OrbitConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT + " = " + contextRoot);
		LOG.info(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER + " = " + jdbcDriver);
		LOG.info(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_URL + " = " + jdbcURL);
		LOG.info(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME + " = " + jdbcUsername);
		LOG.info(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD + " = " + jdbcPassword);

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
		String driver = getProperty(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER, String.class);
		String url = getProperty(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_URL, String.class);
		String username = getProperty(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME, String.class);
		String password = getProperty(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD, String.class);
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	protected Connection getConnection() {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	@Override
	public String getName() {
		String name = getProperty(OrbitConstants.COMPONENT_INDEX_SERVICE_NAME, String.class);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = getProperty(OrbitConstants.COMPONENT_INDEX_SERVICE_HOST_URL, String.class);
		if (hostURL != null) {
			return hostURL;
		}

		// default global config
		String globalHostURL = (String) this.properties.get(OrbitConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}

		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = getProperty(OrbitConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT, String.class);
		return contextRoot;
	}

	/**
	 * 
	 * @param e
	 * @throws IndexServiceException
	 */
	protected void handleSQLException(SQLException e) throws IndexServiceException {
		e.printStackTrace();
		throw new IndexServiceException(StatusDTO.RESP_500, e.getMessage(), e);
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId) throws IndexServiceException {
		LOG.info("getIndexItems(String indexProviderId) indexProviderId = " + indexProviderId);

		List<IndexItem> indexItems = new ArrayList<IndexItem>();
		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);
			List<IndexItemVO> indexItemVOs = tableHandler.getIndexItems(conn);
			LOG.info("---------------------------------------------------------------------------");
			if (indexItemVOs != null) {
				for (IndexItemVO indexItemVO : indexItemVOs) {
					LOG.info(indexItemVO.toString());
					IndexItem indexItem = IndexServiceDatabaseHelper.INSTANCE.toIndexItem(indexItemVO);
					indexItems.add(indexItem);
				}
			}
			LOG.info("---------------------------------------------------------------------------");

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return indexItems;
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId, String type) throws IndexServiceException {
		LOG.info("getIndexItems(String indexProviderId, String type) indexProviderId = " + indexProviderId + ", type = " + type);

		List<IndexItem> indexItems = new ArrayList<IndexItem>();
		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);
			List<IndexItemVO> indexItemVOs = tableHandler.getIndexItems(conn, type);
			LOG.info("---------------------------------------------------------------------------");
			if (indexItemVOs != null) {
				for (IndexItemVO indexItemVO : indexItemVOs) {
					LOG.info(indexItemVO.toString());
					IndexItem indexItem = IndexServiceDatabaseHelper.INSTANCE.toIndexItem(indexItemVO);
					indexItems.add(indexItem);
				}
			}
			LOG.info("---------------------------------------------------------------------------");

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return indexItems;
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, String type, String name) throws IndexServiceException {
		LOG.info("getIndexItem(String indexProviderId, String type, String name) indexProviderId = " + indexProviderId + ", type = " + type + ", name = " + name);

		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);
			IndexItemVO indexItemVO = tableHandler.getIndexItem(conn, type, name);
			if (indexItemVO != null) {
				LOG.info("---------------------------------------------------------------------------");
				LOG.info(indexItemVO.toString());
				LOG.info("---------------------------------------------------------------------------");

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
	public IndexItem getIndexItem(String indexProviderId, Integer indexItemId) throws IndexServiceException {
		LOG.info("getIndexItem(String indexProviderId, Integer indexItemId) indexProviderId = " + indexProviderId + ", indexItemId = " + indexItemId);

		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);
			IndexItemVO indexItemVO = tableHandler.getIndexItem(conn, indexItemId);
			if (indexItemVO != null) {
				LOG.info("---------------------------------------------------------------------------");
				LOG.info(indexItemVO.toString());
				LOG.info("---------------------------------------------------------------------------");

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
	public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IndexServiceException {
		LOG.info("addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) indexProviderId = " + indexProviderId + ", type = " + type + ", name = " + name);

		if (properties == null) {
			LOG.info("    properties: n/a");
		} else {
			LOG.info("    properties:");
			for (Iterator<String> itor = properties.keySet().iterator(); itor.hasNext();) {
				String propName = itor.next();
				Object propValue = properties.get(propName);
				LOG.info("        " + propName + " = " + propValue);
			}
		}

		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);

			String propertiesString = JSONUtil.toJsonString(properties);
			Date createTime = new Date();
			Date lastUpdateTime = createTime;

			IndexItemVO newIndexItemVO = tableHandler.insert(conn, type, name, propertiesString, createTime, lastUpdateTime);
			if (newIndexItemVO != null) {
				LOG.info("---------------------------------------------------------------------------");
				LOG.info(newIndexItemVO.toString());
				LOG.info("---------------------------------------------------------------------------");

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
	public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws IndexServiceException {
		LOG.info("removeIndexItem(String indexProviderId, Integer indexItemId) indexProviderId = " + indexProviderId + ", indexItemId = " + indexItemId);

		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);
			return tableHandler.delete(conn, indexItemId);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean hasProperty(String indexProviderId, Integer indexItemId, String propName) throws IndexServiceException {
		LOG.info("hasProperty(String indexProviderId, Integer indexItemId, String propName) indexProviderId = " + indexProviderId + ", indexItemId = " + indexItemId + ", propName = " + propName);

		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);

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
	public Map<String, Object> getProperties(String indexProviderId, Integer indexItemId) throws IndexServiceException {
		LOG.info("getProperties(String indexProviderId, Integer indexItemId) indexProviderId = " + indexProviderId + ", indexItemId = " + indexItemId);

		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);

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
	public Object getProperty(String indexProviderId, Integer indexItemId, String propName) throws IndexServiceException {
		LOG.info("getProperty(String indexProviderId, Integer indexItemId, String propName) indexProviderId = " + indexProviderId + ", indexItemId = " + indexItemId + ", propName = " + propName);

		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);

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
	public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws IndexServiceException {
		LOG.info("setProperties(String indexProviderId, Map<String, Object> properties) indexProviderId = " + indexProviderId);

		if (properties == null) {
			LOG.info("    properties: n/a");
		} else {
			LOG.info("    properties:");
			for (Iterator<String> itor = properties.keySet().iterator(); itor.hasNext();) {
				String propName = itor.next();
				Object propValue = properties.get(propName);
				LOG.info("        " + propName + " = " + propValue);
			}
		}

		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);

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
	public boolean removeProperty(String indexProviderId, Integer indexItemId, List<String> propNames) throws IndexServiceException {
		LOG.info("removeProperty(String indexProviderId, Integer indexItemId, List<String> propNames) indexProviderId = " + indexProviderId + ", indexItemId = " + indexItemId);

		if (properties == null) {
			LOG.info("    propNames: n/a");
		} else {
			LOG.info("    propNames:");
			for (String propName : propNames) {
				LOG.info("        propName = " + propName);
			}
		}

		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);

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
