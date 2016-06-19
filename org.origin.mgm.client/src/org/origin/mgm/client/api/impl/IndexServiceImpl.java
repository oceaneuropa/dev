package org.origin.mgm.client.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexService;
import org.origin.mgm.client.api.IndexServiceConfiguration;
import org.origin.mgm.client.ws.IndexServiceClient;
import org.origin.mgm.model.dto.IndexItemDTO;

public class IndexServiceImpl extends IndexService {

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
	public List<IndexItem> getIndexItems() throws IOException {
		return doGetIndexItems(null, null);
	}

	@Override
	public List<IndexItem> getIndexItemsByNamespace(String namespace) throws IOException {
		return doGetIndexItems(null, namespace);
	}

	@Override
	public List<IndexItem> getIndexItemsByIndexProvider(String indexProviderId) throws IOException {
		return doGetIndexItems(indexProviderId, null);
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId, String namespace) throws IOException {
		return doGetIndexItems(indexProviderId, namespace);
	}

	/**
	 * 
	 * @param indexProviderId
	 * @param namespace
	 * @return
	 * @throws IOException
	 */
	protected List<IndexItem> doGetIndexItems(String indexProviderId, String namespace) throws IOException {
		List<IndexItem> indexItems = new ArrayList<IndexItem>();
		try {
			List<IndexItemDTO> indexItemDTOs = getClient().getIndexItems(indexProviderId, namespace);
			for (IndexItemDTO indexItemDTO : indexItemDTOs) {
				String currIndexProviderId = indexItemDTO.getIndexProviderId();
				String currNamespace = indexItemDTO.getNamespace();
				String currName = indexItemDTO.getName();

				if (indexProviderId != null && !indexProviderId.equals(currIndexProviderId)) {
					System.err.println("IndexItemDTO '" + indexItemDTO.toString() + "' has a different indexProviderId ('" + currIndexProviderId + "') than the specified indexProviderId ('" + indexProviderId + "').");
				}

				if (namespace != null && !namespace.equals(currNamespace)) {
					System.err.println("IndexItemDTO '" + indexItemDTO.toString() + "' has a different namespace ('" + currNamespace + "') than the specified namespace  ('" + namespace + "').");
				}

				IndexItemImpl indexItem = new IndexItemImpl(this.config, currIndexProviderId, currNamespace, currName);
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
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		return false;
	}

}
