package org.orbit.infra.connector.indexes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemUpdater;
import org.orbit.infra.api.indexes.IndexItemUpdaterSupport;
import org.orbit.infra.api.indexes.IndexProviderItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.connector.util.ClientModelConverter;
import org.orbit.infra.model.indexes.IndexItemDTO;
import org.orbit.infra.model.indexes.IndexProviderItemDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.StatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class IndexServiceClientImpl extends ServiceClientImpl<IndexServiceClient, IndexServiceWSClient> implements IndexServiceClient /* , InternalProxyService */ {

	protected static Logger LOG = LoggerFactory.getLogger(IndexServiceClientImpl.class);

	protected Map<String, Object> properties;
	protected IndexServiceWSClient client;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();
	protected boolean isProxy = false;

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public IndexServiceClientImpl(ServiceConnector<IndexServiceClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected IndexServiceWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new IndexServiceWSClient(config);
	}

	// ---------------------------------------------------------------------------------------------------
	// Index Providers
	// ---------------------------------------------------------------------------------------------------
	@Override
	public List<IndexProviderItem> getIndexProviders() throws IOException {
		List<IndexProviderItem> items = new ArrayList<IndexProviderItem>();
		try {
			List<IndexProviderItemDTO> DTOs = getWSClient().getIndexProviders();
			if (DTOs != null) {
				for (IndexProviderItemDTO DTO : DTOs) {
					IndexProviderItem item = ClientModelConverter.INDEX_SERVICE.toIndexProvider(DTO);
					if (item != null) {
						items.add(item);
					}
				}
			}
		} catch (ClientException e) {
			throw new IOException(e);
		}
		return items;
	}

	@Override
	public IndexProviderItem addIndexProvider(String id, String name, String description) throws IOException {
		IndexProviderItem item = null;
		try {
			IndexProviderItemDTO DTO = getWSClient().addIndexProvider(id, name, description);
			if (DTO != null) {
				item = ClientModelConverter.INDEX_SERVICE.toIndexProvider(DTO);
			}
		} catch (ClientException e) {
			throw new IOException(e);
		}
		return item;
	}

	@Override
	public boolean deleteIndexProvider(String id) throws IOException {
		try {
			StatusDTO status = getWSClient().deleteIndexProvider(id);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw new IOException(e);
		}
		return false;
	}

	// ---------------------------------------------------------------------------------------------------
	// Index Items
	// ---------------------------------------------------------------------------------------------------
	@Override
	public List<IndexItem> getIndexItems(String indexProviderId) throws IOException {
		return doGetIndexItems(indexProviderId, null);
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId, String type) throws IOException {
		return doGetIndexItems(indexProviderId, type);
	}

	/**
	 * @param indexItemId
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param indexItemDTO
	 */
	protected void checkIndexItem(Integer indexItemId, String indexProviderId, String type, String name, IndexItemDTO indexItemDTO) {
		Integer currIndexItemId = indexItemDTO.getIndexItemId();
		String currIndexProviderId = indexItemDTO.getIndexProviderId();
		String currType = indexItemDTO.getType();
		String currName = indexItemDTO.getName();

		if (indexItemId != null && currIndexItemId != null && indexItemId.intValue() != currIndexItemId.intValue()) {
			System.err.println("IndexItemDTO '" + indexItemDTO.getIndexItemId() + "' has a different indexItemId ('" + currIndexItemId + "') than the specified indexItemId ('" + indexItemId + "').");
		}
		if (indexProviderId != null && !indexProviderId.equals(currIndexProviderId)) {
			System.err.println("IndexItemDTO '" + indexItemDTO.getIndexItemId() + "' has a different indexProviderId ('" + currIndexProviderId + "') than the specified indexProviderId ('" + indexProviderId + "').");
		}
		if (type != null && !type.equals(currType)) {
			System.err.println("IndexItemDTO '" + indexItemDTO.getIndexItemId() + "' has a different type ('" + currType + "') than the specified type  ('" + type + "').");
		}
		if (name != null && !name.equals(currName)) {
			System.err.println("IndexItemDTO '" + indexItemDTO.getIndexItemId() + "' has a different name ('" + currName + "') than the specified name  ('" + name + "').");
		}
	}

	/**
	 * 
	 * @param indexProviderId
	 * @param type
	 * @return
	 * @throws IOException
	 */
	protected List<IndexItem> doGetIndexItems(String indexProviderId, String type) throws IOException {
		List<IndexItem> indexItems = new ArrayList<IndexItem>();
		try {
			List<IndexItemDTO> indexItemDTOs = getWSClient().getIndexItems(indexProviderId, type);
			for (IndexItemDTO indexItemDTO : indexItemDTOs) {
				// Integer currIndexItemId = indexItemDTO.getIndexItemId();
				// String currIndexProviderId = indexItemDTO.getIndexProviderId();
				// String currType = indexItemDTO.getType();
				// String currName = indexItemDTO.getName();
				//
				// // Map<String, Object> currProperties = indexItemDTO.getProperties();
				// String propertiesString = indexItemDTO.getPropertiesString();
				// Map<String, Object> currProperties = JSONUtil.toProperties(propertiesString);
				//
				// checkIndexItem(null, indexProviderId, type, null, indexItemDTO);
				//
				// IndexItemImpl indexItem = new IndexItemImpl(currIndexItemId, currIndexProviderId, currType, currName, currProperties);
				IndexItem indexItem = ClientModelConverter.INDEX_SERVICE.toIndexItem(this, indexItemDTO);
				indexItems.add(indexItem);
			}
		} catch (ClientException e) {
			// e.printStackTrace();
			throw new IOException(e);
		}
		return indexItems;
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, String type, String name) throws IOException {
		IndexItem indexItem = null;
		try {
			IndexItemDTO indexItemDTO = getWSClient().getIndexItem(indexProviderId, type, name);
			if (indexItemDTO != null) {
				// Integer currIndexItemId = indexItemDTO.getIndexItemId();
				// String currIndexProviderId = indexItemDTO.getIndexProviderId();
				// String currType = indexItemDTO.getType();
				// String currName = indexItemDTO.getName();
				//
				// // Map<String, Object> currProperties = indexItemDTO.getProperties();
				// String propertiesString = indexItemDTO.getPropertiesString();
				// Map<String, Object> currProperties = JSONUtil.toProperties(propertiesString);

				// checkIndexItem(null, indexProviderId, type, name, indexItemDTO);

				// indexItem = new IndexItemImpl(currIndexItemId, currIndexProviderId, currType, currName, currProperties);
				indexItem = ClientModelConverter.INDEX_SERVICE.toIndexItem(this, indexItemDTO);
			}
		} catch (ClientException e) {
			LOG.error("ClientException: getIndexItem(String indexProviderId, String type, String name) indexProviderId = " + indexProviderId + ", type = " + type + ", name = " + name);
			LOG.error("ClientException: " + e.getMessage());
			// e.printStackTrace();
			// throw new IOException(e);
		}
		return indexItem;
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
		IndexItem indexItem = null;
		try {
			IndexItemDTO indexItemDTO = getWSClient().getIndexItem(indexProviderId, indexItemId);
			if (indexItemDTO != null) {
				// Integer currIndexItemId = indexItemDTO.getIndexItemId();
				// String currIndexProviderId = indexItemDTO.getIndexProviderId();
				// String currType = indexItemDTO.getType();
				// String currName = indexItemDTO.getName();
				//
				// // Map<String, Object> currProperties = indexItemDTO.getProperties();
				// String propertiesString = indexItemDTO.getPropertiesString();
				// Map<String, Object> currProperties = JSONUtil.toProperties(propertiesString);
				//
				// checkIndexItem(indexItemId, null, null, null, indexItemDTO);
				//
				// indexItem = new IndexItemImpl(currIndexItemId, currIndexProviderId, currType, currName, currProperties);
				indexItem = ClientModelConverter.INDEX_SERVICE.toIndexItem(this, indexItemDTO);
			}
		} catch (ClientException e) {
			// e.printStackTrace();
			throw new IOException(e);
		}
		return indexItem;
	}

	@Override
	public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException {
		IndexItem newIndexItem = null;
		try {
			this.indexItemUpdaterSupport.addIndexItem(indexProviderId, type, name, properties);

			IndexItemDTO indexItemDTO = getWSClient().addIndexItem(indexProviderId, type, name, properties);
			if (indexItemDTO != null) {
				// Integer currIndexItemId = indexItemDTO.getIndexItemId();
				// String currIndexProviderId = indexItemDTO.getIndexProviderId();
				// String currType = indexItemDTO.getType();
				// String currName = indexItemDTO.getName();
				//
				// // Map<String, Object> currProperties = newIndexItemDTO.getProperties();
				// String propertiesString = indexItemDTO.getPropertiesString();
				// Map<String, Object> currProperties = JSONUtil.toProperties(propertiesString);
				//
				// newIndexItem = new IndexItemImpl(currIndexItemId, currIndexProviderId, currType, currName, currProperties);
				newIndexItem = ClientModelConverter.INDEX_SERVICE.toIndexItem(this, indexItemDTO);
			}

		} catch (ClientException e) {
			LOG.error("ClientException: getIndexItem(String indexProviderId, String type, String name) indexProviderId = " + indexProviderId + ", type = " + type + ", name = " + name);
			LOG.error("ClientException: " + e.getMessage());
			// e.printStackTrace();
			// throw new IOException(e);
		}
		return newIndexItem;
	}

	@Override
	public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
		try {
			StatusDTO status = getWSClient().removeIndexItem(indexProviderId, indexItemId);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			// e.printStackTrace();
			throw new IOException(e);
		}
		return false;
	}

	@Override
	public boolean removeIndexItems(String indexProviderId) throws IOException {
		try {
			StatusDTO status = getWSClient().removeIndexItems(indexProviderId);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			// e.printStackTrace();
			throw new IOException(e);
		}
		return false;
	}

	@Override
	public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws IOException {
		try {
			this.indexItemUpdaterSupport.setProperties(indexProviderId, indexItemId, properties);

			StatusDTO status = getWSClient().setProperties(indexProviderId, indexItemId, properties);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			// e.printStackTrace();
			throw new IOException(e);
		}
		return false;
	}

	@Override
	public boolean setProperty(String indexProviderId, Integer indexItemId, String propName, Object propValue, String propType) throws IOException {
		try {
			StatusDTO status = getWSClient().setProperty(indexProviderId, indexItemId, propName, propValue, propType);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			// e.printStackTrace();
			throw new IOException(e);
		}
		return false;
	}

	@Override
	public boolean removeProperties(String indexProviderId, Integer indexItemId, List<String> propertyNames) throws IOException {
		try {
			if (propertyNames == null || propertyNames.isEmpty()) {
				return false;
			}
			StatusDTO status = getWSClient().removeProperties(indexProviderId, indexItemId, propertyNames);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			// e.printStackTrace();
			throw new IOException(e);
		}
		return false;
	}

	@Override
	public boolean isProxy() {
		return this.isProxy;
	}

	@Override
	public void setProxy(boolean isProxy) {
		this.isProxy = isProxy;
	}

	protected IndexItemUpdaterSupport indexItemUpdaterSupport = new IndexItemUpdaterSupport();

	@Override
	public List<IndexItemUpdater> getIndexItemUpdaters() {
		return this.indexItemUpdaterSupport.getUpdaters();
	}

	@Override
	public void addIndexItemUpdater(IndexItemUpdater updater) {
		this.indexItemUpdaterSupport.addUpdater(updater);
	}

	@Override
	public void removeIndexItemUpdater(IndexItemUpdater updater) {
		this.indexItemUpdaterSupport.removeUpdater(updater);
	}

}

// void update(Map<Object, Object> properties);

// @Override
// public boolean ping() {
// return this.client.doPing();
// }

// @Override
// public String echo(String message) throws IOException {
// try {
// return this.client.echo(message);
// } catch (ClientException e) {
// throw new IOException(e);
// }
// }

// @Override
// public boolean sendCommand(String command, Map<String, Object> parameters) throws IOException {
// try {
// StatusDTO status = getClient().sendCommand(command, parameters);
// return (status != null && status.success()) ? true : false;
//
// } catch (ClientException e) {
// throw new IOException(e);
// }
// }

// @Override
// public List<IndexItem> getIndexItems() throws IOException {
// return doGetIndexItems(null, null);
// }

/// **
// * Whether whether an index item exists.
// *
// * @param indexProviderId
// * @param type
// * @param name
// * @return
// * @throws IOException
// */
// public boolean hasIndexItem(String indexProviderId, String type, String name) throws IOException;

/// **
// * Whether whether an index item exists.
// *
// * @param indexItemId
// * @return
// * @throws IOException
// */
// public boolean hasIndexItem(Integer indexItemId) throws IOException;

// @Override
// public boolean hasIndexItem(String indexProviderId, String type, String name) throws IOException {
// try {
// return getClient().indexItemExists(indexProviderId, type, name);
// } catch (ClientException e) {
// e.printStackTrace();
// throw new IOException(e);
// }
// return false;
// }

// @Override
// public boolean hasIndexItem(Integer indexItemId) throws IOException {
// try {
// return getClient().indexItemExists(indexItemId);
// } catch (ClientException e) {
// e.printStackTrace();
// throw new IOException(e);
// }
// return false;
// }

// /** implement IAdaptable interface */
// @Override
// public <T> T getAdapter(Class<T> adapter) {
// T result = this.adaptorSupport.getAdapter(adapter);
// if (result != null) {
// return result;
// }
// return null;
// }
//
// @Override
// public <T> void adapt(Class<T> clazz, T object) {
// this.adaptorSupport.adapt(clazz, object);
// }
//
// @Override
// public <T> void adapt(Class<T>[] classes, T object) {
// this.adaptorSupport.adapt(classes, object);
// }

// /**
// *
// * @param config
// */
// public IndexServiceClientImpl(Map<String, Object> properties) {
// this.properties = checkProperties(properties);
// initClient();
// }

// private Map<String, Object> checkProperties(Map<String, Object> properties) {
// if (properties == null) {
// properties = new HashMap<String, Object>();
// }
// return properties;
// }

// ------------------------------------------------------------------------------------------------
// Configuration methods
// ------------------------------------------------------------------------------------------------
// @Override
// public String getName() {
// String name = (String) this.properties.get(InfraConstants.INDEX_SERVICE_NAME);
// return name;
// }

// @Override
// public String getURL() {
// String hostURL = (String) this.properties.get(InfraConstants.INDEX_SERVICE_HOST_URL);
// String contextRoot = (String) this.properties.get(InfraConstants.INDEX_SERVICE_CONTEXT_ROOT);
// return hostURL + contextRoot;
// }

// @Override
// public Map<String, Object> getProperties() {
// return this.properties;
// }

// /**
// * Update properties. Re-initiate web service client if host URL or context root is changed.
// *
// * @param properties
// */
// @Override
// public void update(Map<String, Object> properties) {
// this.properties = checkProperties(properties);
// initClient();
// }

// protected void initClient() {
// String realm = (String) properties.get(WSClientConstants.REALM);
// String accessToken = (String) this.properties.get(WSClientConstants.ACCESS_TOKEN);
// String url = (String) properties.get(WSClientConstants.URL);
//
// String orbitRealm = (String) properties.get(InfraConstants.ORBIT_REALM);
// String orbitIndexServiceURL = (String) properties.get(InfraConstants.ORBIT_INDEX_SERVICE_URL);
// if (realm == null && orbitRealm != null) {
// realm = orbitRealm;
// }
// if (url == null && orbitIndexServiceURL != null) {
// url = orbitIndexServiceURL;
// }
// WSClientConfiguration clientConfig = WSClientConfiguration.create(this.properties);
// this.client = new IndexServiceWSClient(clientConfig);
// }

// protected IndexServiceWSClient getClient() {
// return this.client;
// }

// /**
// * Get web service client configuration.
// *
// * @param properties
// * @return
// */
// protected WSClientConfiguration getClientConfiguration(Map<String, Object> properties) {
// String realm = (String) properties.get(WSClientConstants.REALM);
// String accessToken = (String) this.properties.get(WSClientConstants.ACCESS_TOKEN);
// String url = (String) properties.get(InfraConstants.INDEX_SERVICE_HOST_URL);
// String contextRoot = (String) properties.get(InfraConstants.INDEX_SERVICE_CONTEXT_ROOT);
//
// return WSClientConfiguration.create(realm, accessToken, url, contextRoot);
// }
