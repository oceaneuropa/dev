package org.origin.mgm.client.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexService;
import org.origin.mgm.client.api.IndexServiceConfiguration;
import org.origin.mgm.client.ws.IndexServiceWSClient;
import org.origin.mgm.model.dto.IndexItemDTO;

public class IndexServiceImpl implements IndexService {

	protected AdaptorSupport adaptorSupport = new AdaptorSupport();
	protected IndexServiceConfiguration config;

	/**
	 * 
	 * @param config
	 */
	public IndexServiceImpl(IndexServiceConfiguration config) {
		this.config = config;
	}

	protected IndexServiceWSClient getClient() {
		return this.config.getClient();
	}

	@Override
	public IndexServiceConfiguration getConfiguration() {
		return this.config;
	}

	@Override
	public int ping() {
		try {
			return getClient().ping();
		} catch (ClientException e) {
			e.printStackTrace();
			return -1;
		}
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

				IndexItemImpl indexItem = new IndexItemImpl(this.config, currIndexItemId, currIndexProviderId, currType, currName, currProperties);
				indexItems.add(indexItem);
			}
		} catch (ClientException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		return indexItems;
	}

	// @Override
	public boolean hasIndexItem(String indexProviderId, String type, String name) throws IOException {
		// try {
		// return getClient().indexItemExists(indexProviderId, type, name);
		// } catch (ClientException e) {
		// e.printStackTrace();
		// throw new IOException(e);
		// }
		return false;
	}

	// @Override
	public boolean hasIndexItem(Integer indexItemId) throws IOException {
		// try {
		// return getClient().indexItemExists(indexItemId);
		// } catch (ClientException e) {
		// e.printStackTrace();
		// throw new IOException(e);
		// }
		return false;
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

				indexItem = new IndexItemImpl(this.config, currIndexItemId, currIndexProviderId, currType, currName, currProperties);
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

				indexItem = new IndexItemImpl(this.config, currIndexItemId, currIndexProviderId, currType, currName, currProperties);
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
