package org.origin.mgm.service.impl2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.util.PropertyUtil;
import org.origin.mgm.OriginConstants;
import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.runtime.IndexItem;
import org.origin.mgm.model.vo.IndexItemVO;
import org.origin.mgm.service.IndexService;
import org.origin.mgm.service.impl.IndexServiceDatabaseHelper;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class IndexServiceDatabaseSimpleImpl implements IndexService {

	protected Properties configIniProps;
	protected Map<Object, Object> configProps = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;

	/**
	 * 
	 * @param configIniProps
	 */
	public IndexServiceDatabaseSimpleImpl(Properties configIniProps) {
		this.configIniProps = configIniProps;
	}

	/**
	 * Start IndexService
	 * 
	 */
	public void start(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".start()");

		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, configProps, OriginConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OriginConstants.COMPONENT_INDEX_SERVICE_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OriginConstants.COMPONENT_INDEX_SERVICE_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OriginConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, configProps, OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, configProps, OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD);

		updateProperties(configProps);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(IndexService.class, this, props);
	}

	/**
	 * Stop IndexService
	 * 
	 */
	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> T getProperty(Object key, Class<T> valueClass) {
		// Config properties from bundle context or from system/env properties takes precedence over properties defined in config.ini file.
		Object object = this.configProps.get(key);
		if (object != null && valueClass.isAssignableFrom(object.getClass())) {
			return (T) object;
		}
		// If config properties cannot be found, read from config.ini file
		if (String.class.equals(valueClass)) {
			String value = this.configIniProps.getProperty(key.toString());
			if (value != null) {
				return (T) value;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param props
	 */
	public synchronized void updateProperties(Map<Object, Object> configProps) {
		System.out.println(getClass().getSimpleName() + ".updateProperties()");

		if (configProps == null) {
			configProps = new HashMap<Object, Object>();
		}

		String globalHostURL = (String) configProps.get(OriginConstants.ORBIT_HOST_URL);
		String name = (String) configProps.get(OriginConstants.COMPONENT_INDEX_SERVICE_NAME);
		String hostURL = (String) configProps.get(OriginConstants.COMPONENT_INDEX_SERVICE_HOST_URL);
		String contextRoot = (String) configProps.get(OriginConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT);
		String jdbcDriver = (String) configProps.get(OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER);
		String jdbcURL = (String) configProps.get(OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_URL);
		String jdbcUsername = (String) configProps.get(OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME);
		String jdbcPassword = (String) configProps.get(OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD);

		System.out.println(OriginConstants.ORBIT_HOST_URL + " = " + globalHostURL);
		System.out.println(OriginConstants.COMPONENT_INDEX_SERVICE_NAME + " = " + name);
		System.out.println(OriginConstants.COMPONENT_INDEX_SERVICE_HOST_URL + " = " + hostURL);
		System.out.println(OriginConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT + " = " + contextRoot);
		System.out.println(OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER + " = " + jdbcDriver);
		System.out.println(OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_URL + " = " + jdbcURL);
		System.out.println(OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME + " = " + jdbcUsername);
		System.out.println(OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD + " = " + jdbcPassword);

		this.configProps = configProps;
		this.databaseProperties = getConnectionProperties(this.configProps);
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	protected synchronized Properties getConnectionProperties(Map<Object, Object> props) {
		String driver = getProperty(OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER, String.class);
		String url = getProperty(OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_URL, String.class);
		String username = getProperty(OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME, String.class);
		String password = getProperty(OriginConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD, String.class);
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	protected Connection getConnection() {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	@Override
	public String getName() {
		String name = getProperty(OriginConstants.COMPONENT_INDEX_SERVICE_NAME, String.class);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = getProperty(OriginConstants.COMPONENT_INDEX_SERVICE_HOST_URL, String.class);
		if (hostURL != null) {
			return hostURL;
		}

		// default global config
		String globalHostURL = (String) this.configProps.get(OriginConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}

		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = getProperty(OriginConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT, String.class);
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
		List<IndexItem> indexItems = new ArrayList<IndexItem>();
		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);
			List<IndexItemVO> indexItemVOs = tableHandler.getIndexItems(conn);
			if (indexItemVOs != null) {
				for (IndexItemVO indexItemVO : indexItemVOs) {
					IndexItem indexItem = IndexServiceDatabaseHelper.INSTANCE.toIndexItem(indexItemVO);
					indexItems.add(indexItem);
				}
			}
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return indexItems;
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId, String type) throws IndexServiceException {
		List<IndexItem> indexItems = new ArrayList<IndexItem>();
		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);
			List<IndexItemVO> indexItemVOs = tableHandler.getIndexItems(conn, type);
			if (indexItemVOs != null) {
				for (IndexItemVO indexItemVO : indexItemVOs) {
					IndexItem indexItem = IndexServiceDatabaseHelper.INSTANCE.toIndexItem(indexItemVO);
					indexItems.add(indexItem);
				}
			}
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return indexItems;
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, String type, String name) throws IndexServiceException {
		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);
			IndexItemVO indexItemVO = tableHandler.getIndexItem(conn, type, name);
			if (indexItemVO != null) {
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
		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);
			IndexItemVO indexItemVO = tableHandler.getIndexItem(conn, indexItemId);
			if (indexItemVO != null) {
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
		Connection conn = getConnection();
		try {
			IndexItemsSimpleTableHandler tableHandler = IndexItemsSimpleTableHandler.getInstance(conn, indexProviderId);

			String propertiesString = JSONUtil.toJsonString(properties);
			Date createTime = new Date();
			Date lastUpdateTime = createTime;

			IndexItemVO newIndexItemVO = tableHandler.insert(conn, type, name, propertiesString, createTime, lastUpdateTime);
			if (newIndexItemVO != null) {
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
