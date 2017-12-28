package org.orbit.infra.connector.indexes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.connector.OrbitConstants;
import org.orbit.infra.model.indexes.IndexItemDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.util.StringUtil;

public class IndexServiceImpl implements IndexService {

	protected Map<Object, Object> properties;
	protected IndexServiceWSClient client;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param config
	 */
	public IndexServiceImpl(Map<Object, Object> properties) {
		this.properties = checkProperties(properties);
		initClient();
	}

	// ------------------------------------------------------------------------------------------------
	// Configuration methods
	// ------------------------------------------------------------------------------------------------
	@Override
	public String getName() {
		String name = (String) this.properties.get(OrbitConstants.INDEX_SERVICE_NAME);
		return name;
	}

	@Override
	public String getURL() {
		String hostURL = (String) this.properties.get(OrbitConstants.INDEX_SERVICE_HOST_URL);
		String contextRoot = (String) this.properties.get(OrbitConstants.INDEX_SERVICE_CONTEXT_ROOT);
		return hostURL + contextRoot;
	}

	@Override
	public Map<Object, Object> getProperties() {
		return this.properties;
	}

	/**
	 * Update properties. Re-initiate web service client if host URL or context root is changed.
	 * 
	 * @param properties
	 */
	// @Override
	public void update(Map<Object, Object> properties) {
		String oldUrl = (String) this.properties.get(OrbitConstants.INDEX_SERVICE_HOST_URL);
		String oldContextRoot = (String) this.properties.get(OrbitConstants.INDEX_SERVICE_CONTEXT_ROOT);

		properties = checkProperties(properties);
		this.properties.putAll(properties);

		String newUrl = (String) properties.get(OrbitConstants.INDEX_SERVICE_HOST_URL);
		String newContextRoot = (String) properties.get(OrbitConstants.INDEX_SERVICE_CONTEXT_ROOT);

		boolean reinitClient = false;
		if (!StringUtil.equals(oldUrl, newUrl) || !StringUtil.equals(oldContextRoot, newContextRoot)) {
			reinitClient = true;
		}
		if (reinitClient) {
			initClient();
		}
	}

	private Map<Object, Object> checkProperties(Map<Object, Object> properties) {
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		return properties;
	}

	protected void initClient() {
		ClientConfiguration clientConfig = getClientConfiguration(this.properties);
		this.client = new IndexServiceWSClient(clientConfig);
	}

	protected IndexServiceWSClient getClient() {
		return this.client;
	}

	/**
	 * Get web service client configuration.
	 * 
	 * @param properties
	 * @return
	 */
	protected ClientConfiguration getClientConfiguration(Map<Object, Object> properties) {
		String realm = (String) properties.get(OrbitConstants.REALM);
		String username = (String) properties.get(OrbitConstants.USERNAME);
		String url = (String) properties.get(OrbitConstants.INDEX_SERVICE_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.INDEX_SERVICE_CONTEXT_ROOT);
		return ClientConfiguration.create(realm, username, url, contextRoot);
	}

	@Override
	public boolean ping() {
		return this.client.doPing();
	}

	@Override
	public List<IndexItem> getIndexItems() throws IOException {
		return doGetIndexItems(null, null);
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId) throws IOException {
		return doGetIndexItems(indexProviderId, null);
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId, String type) throws IOException {
		return doGetIndexItems(indexProviderId, type);
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
			List<IndexItemDTO> indexItemDTOs = getClient().getIndexItems(indexProviderId, type);
			for (IndexItemDTO indexItemDTO : indexItemDTOs) {
				Integer currIndexItemId = indexItemDTO.getIndexItemId();
				String currIndexProviderId = indexItemDTO.getIndexProviderId();
				String currType = indexItemDTO.getType();
				String currName = indexItemDTO.getName();

				// Map<String, Object> currProperties = indexItemDTO.getProperties();
				String propertiesString = indexItemDTO.getPropertiesString();
				Map<String, Object> currProperties = JSONUtil.toProperties(propertiesString);

				checkIndexItem(null, indexProviderId, type, null, indexItemDTO);

				IndexItemImpl indexItem = new IndexItemImpl(currIndexItemId, currIndexProviderId, currType, currName, currProperties);
				indexItems.add(indexItem);
			}
		} catch (ClientException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		return indexItems;
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, String type, String name) throws IOException {
		IndexItem indexItem = null;
		try {
			IndexItemDTO indexItemDTO = getClient().getIndexItem(indexProviderId, type, name);
			if (indexItemDTO != null) {
				Integer currIndexItemId = indexItemDTO.getIndexItemId();
				String currIndexProviderId = indexItemDTO.getIndexProviderId();
				String currType = indexItemDTO.getType();
				String currName = indexItemDTO.getName();

				// Map<String, Object> currProperties = indexItemDTO.getProperties();
				String propertiesString = indexItemDTO.getPropertiesString();
				Map<String, Object> currProperties = JSONUtil.toProperties(propertiesString);

				checkIndexItem(null, indexProviderId, type, name, indexItemDTO);

				indexItem = new IndexItemImpl(currIndexItemId, currIndexProviderId, currType, currName, currProperties);
			}
		} catch (ClientException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		return indexItem;
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
		IndexItem indexItem = null;
		try {
			IndexItemDTO indexItemDTO = getClient().getIndexItem(indexProviderId, indexItemId);
			if (indexItemDTO != null) {
				Integer currIndexItemId = indexItemDTO.getIndexItemId();
				String currIndexProviderId = indexItemDTO.getIndexProviderId();
				String currType = indexItemDTO.getType();
				String currName = indexItemDTO.getName();

				// Map<String, Object> currProperties = indexItemDTO.getProperties();
				String propertiesString = indexItemDTO.getPropertiesString();
				Map<String, Object> currProperties = JSONUtil.toProperties(propertiesString);

				checkIndexItem(indexItemId, null, null, null, indexItemDTO);

				indexItem = new IndexItemImpl(currIndexItemId, currIndexProviderId, currType, currName, currProperties);
			}
		} catch (ClientException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		return indexItem;
	}

	@Override
	public boolean sendCommand(String command, Map<String, Object> parameters) throws IOException {
		try {
			StatusDTO status = getClient().sendCommand(command, parameters);
			return (status != null && status.success()) ? true : false;

		} catch (ClientException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
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

	/** implement IAdaptable interface */
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

}

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