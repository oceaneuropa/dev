package org.orbit.infra.runtime.indexes.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.platform.sdk.http.AccessTokenSupport;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class IndexServiceIMImpl implements IndexService, LifecycleAware {

	public static class IndexProviderItem {
		protected String indexProviderId;
		protected List<IndexItem> indexItems = new ArrayList<IndexItem>();

		public IndexProviderItem(String indexProviderId) {
			this.indexProviderId = indexProviderId;
		}

		public String getIndexProviderId() {
			return this.indexProviderId;
		}

		public void setIndexProviderId(String indexProviderId) {
			this.indexProviderId = indexProviderId;
		}

		public List<IndexItem> getIndexItems() {
			return this.indexItems;
		}

		public List<IndexItem> getIndexItems(String type) {
			List<IndexItem> theIndexItem = new ArrayList<IndexItem>();
			if (type != null) {
				for (IndexItem currIndexItem : this.indexItems) {
					if (type.equals(currIndexItem.getType())) {
						theIndexItem.add(currIndexItem);
					}
				}
			}
			return theIndexItem;
		}

		public void setIndexItems(List<IndexItem> indexItems) {
			this.indexItems = indexItems;
		}

		public synchronized Integer getNextIndexItemId() {
			Integer nextIndexItemId = new Integer(getMaxIndexItemId().intValue() + 1);
			this.maxIndexItemid = nextIndexItemId;
			return nextIndexItemId;
		}

		protected Integer maxIndexItemid = null;

		protected synchronized Integer getMaxIndexItemId() {
			if (this.maxIndexItemid == null) {
				this.maxIndexItemid = new Integer(1);
				for (IndexItem currIndexItem : this.indexItems) {
					if (currIndexItem.getIndexItemId() > this.maxIndexItemid) {
						this.maxIndexItemid = currIndexItem.getIndexItemId();
					}
				}
			}
			return this.maxIndexItemid;
		}

		public synchronized IndexItem getIndexItem(Integer indexItemId) {
			IndexItem indexItem = null;
			if (indexItemId != null) {
				for (IndexItem currIndexItem : this.indexItems) {
					if (indexItemId.equals(currIndexItem.getIndexItemId())) {
						indexItem = currIndexItem;
						break;
					}
				}
			}
			return indexItem;
		}

		public synchronized IndexItem getIndexItem(String name) {
			IndexItem indexItem = null;
			if (name != null) {
				for (IndexItem currIndexItem : this.indexItems) {
					if (name.equals(currIndexItem.getName())) {
						indexItem = currIndexItem;
						break;
					}
				}
			}
			return indexItem;
		}

		public synchronized IndexItem getIndexItem(String type, String name) {
			IndexItem indexItem = null;
			if (type != null && name != null) {
				for (IndexItem currIndexItem : this.indexItems) {
					if (type.equals(currIndexItem.getType()) && name.equals(currIndexItem.getName())) {
						indexItem = currIndexItem;
						break;
					}
				}
			}
			return indexItem;
		}

		public synchronized boolean addIndexItem(IndexItem indexItem) {
			if (indexItem != null && !this.indexItems.contains(indexItem)) {
				return this.indexItems.add(indexItem);
			}
			return false;
		}

		public synchronized boolean removeIndexItem(IndexItem indexItem) {
			if (indexItem != null) {
				return this.indexItems.remove(indexItem);
			}
			return false;
		}

		public synchronized boolean removeIndexItem(Integer indexItemId) {
			if (indexItemId != null) {
				for (IndexItem currIndexItem : this.indexItems) {
					if (indexItemId.equals(currIndexItem.getIndexItemId())) {
						return this.indexItems.remove(currIndexItem);
					}
				}
			}
			return false;
		}
	}

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected ServiceRegistration<?> serviceRegistry;
	protected AccessTokenSupport accessTokenSupport;
	protected Map<String, IndexProviderItem> indexProviderMap = new HashMap<String, IndexProviderItem>();

	/**
	 * 
	 * @param initProperties
	 */
	public IndexServiceIMImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.accessTokenSupport = new AccessTokenSupport(InfraConstants.TOKEN_PROVIDER__ORBIT, OrbitRoles.INDEX_ADMIN);
	}

	/** AccessTokenAware */
	@Override
	public String getAccessToken() {
		String tokenValue = this.accessTokenSupport.getAccessToken();
		return tokenValue;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
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

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
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
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}

		// String globalHostURL = (String) properties.get(InfraConstants.ORBIT_HOST_URL);
		// String name = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_NAME);
		// String hostURL = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_HOST_URL);
		// String contextRoot = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT);
		// String jdbcDriver = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER);
		// String jdbcURL = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_URL);
		// String jdbcUsername = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME);
		// String jdbcPassword = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD);

		// LOG.debug("Properties:");
		// LOG.debug("-----------------------------------------------------------------------------");
		// LOG.debug(InfraConstants.ORBIT_HOST_URL + " = " + globalHostURL);
		// LOG.debug(InfraConstants.COMPONENT_INDEX_SERVICE_NAME + " = " + name);
		// LOG.debug(InfraConstants.COMPONENT_INDEX_SERVICE_HOST_URL + " = " + hostURL);
		// LOG.debug(InfraConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT + " = " + contextRoot);
		// LOG.debug(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER + " = " + jdbcDriver);
		// LOG.debug(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_URL + " = " + jdbcURL);
		// LOG.debug(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME + " = " + jdbcUsername);
		// LOG.debug(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD + " = " + jdbcPassword);
		// LOG.debug("-----------------------------------------------------------------------------");

		this.properties = properties;
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

	protected synchronized IndexProviderItem getIndexProvider(String indexProviderId) {
		IndexProviderItem indexProvider = this.indexProviderMap.get(indexProviderId);
		if (indexProvider == null) {
			indexProvider = new IndexProviderItem(indexProviderId);
			this.indexProviderMap.put(indexProviderId, indexProvider);
		}
		return indexProvider;
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId) throws ServerException {
		List<IndexItem> indexItems = new ArrayList<IndexItem>();

		IndexProviderItem indexProvider = getIndexProvider(indexProviderId);
		List<IndexItem> theIndexItems = indexProvider.getIndexItems();
		indexItems.addAll(theIndexItems);

		return indexItems;
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId, String type) throws ServerException {
		List<IndexItem> indexItems = null;

		if (indexProviderId != null && type != null) {
			IndexProviderItem indexProvider = getIndexProvider(indexProviderId);
			indexItems = indexProvider.getIndexItems(type);
		}

		if (indexItems == null) {
			indexItems = new ArrayList<IndexItem>();
		}
		return indexItems;
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, String type, String name) throws ServerException {
		IndexItem indexItem = null;

		if (indexProviderId != null && type != null) {
			IndexProviderItem indexProvider = getIndexProvider(indexProviderId);
			indexItem = indexProvider.getIndexItem(type, name);
		}

		return indexItem;
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, Integer indexItemId) throws ServerException {
		IndexItem indexItem = null;

		if (indexProviderId != null && indexItemId != null) {
			IndexProviderItem indexProvider = getIndexProvider(indexProviderId);
			indexItem = indexProvider.getIndexItem(indexItemId);
		}

		return indexItem;
	}

	@Override
	public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws ServerException {
		IndexItem indexItem = getIndexItem(indexProviderId, type, name);
		if (indexItem != null) {
			if (properties != null) {
				Date lastUpdateTime = new Date();
				indexItem.getProperties().putAll(properties);
				indexItem.setDateModified(lastUpdateTime);
			}
			return indexItem;
		}

		IndexProviderItem indexProvider = getIndexProvider(indexProviderId);

		Date createTime = new Date();
		Date lastUpdateTime = createTime;

		indexItem = new IndexItem();
		indexItem.setIndexProviderId(indexProviderId);
		indexItem.setType(type);
		indexItem.setName(name);
		if (properties != null) {
			indexItem.getProperties().putAll(properties);
		}
		indexItem.setDateCreated(createTime);
		indexItem.setDateModified(lastUpdateTime);
		indexItem.setIndexItemId(indexProvider.getNextIndexItemId());

		indexProvider.addIndexItem(indexItem);

		return indexItem;
	}

	@Override
	public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws ServerException {
		boolean isRemoved = false;

		if (indexProviderId != null && indexItemId != null) {
			IndexProviderItem indexProvider = getIndexProvider(indexProviderId);
			isRemoved = indexProvider.removeIndexItem(indexItemId);
		}

		return isRemoved;
	}

	@Override
	public boolean removeIndexItems(String indexProviderId) throws ServerException {
		boolean isRemoved = false;

		if (indexProviderId != null) {
			IndexProviderItem indexProvider = this.indexProviderMap.remove(indexProviderId);
			if (indexProvider != null) {
				isRemoved = true;
			}
		}

		return isRemoved;
	}

	@Override
	public boolean hasProperty(String indexProviderId, Integer indexItemId, String propName) throws ServerException {
		boolean hasProperty = false;

		if (indexProviderId != null && indexItemId != null && propName != null) {
			IndexItem indexItem = this.getIndexItem(indexProviderId, indexItemId);
			if (indexItem != null) {
				hasProperty = indexItem.hasProperty(propName);
			}
		}

		return hasProperty;
	}

	@Override
	public Map<String, Object> getProperties(String indexProviderId, Integer indexItemId) throws ServerException {
		Map<String, Object> properties = null;

		if (indexProviderId != null && indexItemId != null) {
			IndexItem indexItem = getIndexItem(indexProviderId, indexItemId);
			if (indexItem != null) {
				properties = indexItem.getProperties();
			}
		}

		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	@Override
	public Object getProperty(String indexProviderId, Integer indexItemId, String propName) throws ServerException {
		Object propValue = null;

		if (indexProviderId != null && indexItemId != null && propName != null) {
			IndexItem indexItem = getIndexItem(indexProviderId, indexItemId);
			if (indexItem != null) {
				propValue = indexItem.getProperty(propName);
			}
		}

		return propValue;
	}

	@Override
	public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws ServerException {
		boolean isUpdated = false;

		if (indexProviderId != null && indexItemId != null && properties != null) {
			IndexItem indexItem = getIndexItem(indexProviderId, indexItemId);
			if (indexItem != null) {
				// Map<String, Object> newProperties = new HashMap<String, Object>();
				// Map<String, Object> oldProperties = indexItem.getProperties();
				// if (oldProperties != null) {
				// newProperties.putAll(oldProperties);
				// }
				// newProperties.putAll(properties);
				// indexItem.setProperties(newProperties);
				Date lastUpdateTime = new Date();

				indexItem.getProperties().putAll(properties);
				indexItem.setDateModified(lastUpdateTime);
				isUpdated = true;
			}
		}

		return isUpdated;
	}

	@Override
	public boolean removeProperties(String indexProviderId, Integer indexItemId, List<String> propNames) throws ServerException {
		boolean isRemoved = false;

		if (indexProviderId != null && indexItemId != null && propNames != null && !propNames.isEmpty()) {
			IndexItem indexItem = getIndexItem(indexProviderId, indexItemId);
			if (indexItem != null) {
				Map<String, Object> newProperties = new HashMap<String, Object>();
				Map<String, Object> oldProperties = indexItem.getProperties();
				if (oldProperties != null) {
					newProperties.putAll(oldProperties);
				}
				for (String propName : propNames) {
					newProperties.remove(propName);
				}

				Date lastUpdateTime = new Date();

				indexItem.setProperties(newProperties);
				indexItem.setDateModified(lastUpdateTime);
				isRemoved = true;
			}
		}

		return isRemoved;
	}

}

// protected static Logger LOG = LoggerFactory.getLogger(IndexServiceIMImpl.class);
