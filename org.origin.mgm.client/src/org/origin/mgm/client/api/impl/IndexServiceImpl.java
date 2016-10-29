package org.origin.mgm.client.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexService;
import org.origin.mgm.client.api.IndexServiceConfiguration;
import org.origin.mgm.client.ws.IndexServiceClient;
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

	protected IndexServiceClient getClient() {
		return this.config.getIndexServiceClient();
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
				String currIndexProviderId = indexItemDTO.getIndexProviderId();
				String currType = indexItemDTO.getType();
				String currName = indexItemDTO.getName();
				Map<String, Object> currProperties = indexItemDTO.getProperties();

				if (indexProviderId != null && !indexProviderId.equals(currIndexProviderId)) {
					System.err.println("IndexItemDTO '" + indexItemDTO.toString() + "' has a different indexProviderId ('" + currIndexProviderId + "') than the specified indexProviderId ('" + indexProviderId + "').");
				}
				if (type != null && !type.equals(currType)) {
					System.err.println("IndexItemDTO '" + indexItemDTO.toString() + "' has a different type ('" + currType + "') than the specified type  ('" + type + "').");
				}

				IndexItemImpl indexItem = new IndexItemImpl(this.config, currIndexProviderId, currType, currName, currProperties);
				indexItems.add(indexItem);
			}
		} catch (ClientException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		return indexItems;
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
