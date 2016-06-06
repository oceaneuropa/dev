package org.origin.mgm.client.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.origin.common.rest.client.ClientException;
import org.origin.mgm.client.api.IndexItemConfigurable;
import org.origin.mgm.client.api.IndexProvider;
import org.origin.mgm.client.api.IndexServiceConfiguration;
import org.origin.mgm.client.ws.IndexServiceClient;
import org.origin.mgm.model.dto.IndexItemDTO;

public class IndexProviderImpl extends IndexProvider {

	protected IndexServiceConfiguration config;
	protected String indexProviderId;

	/**
	 * 
	 * @param indexProviderId
	 * @param config
	 */
	public IndexProviderImpl(String indexProviderId, IndexServiceConfiguration config) {
		this.indexProviderId = indexProviderId;
		this.config = config;
	}

	@Override
	public IndexServiceConfiguration getConfiguration() {
		return this.config;
	}

	@Override
	public String getIndexProviderId() {
		return this.indexProviderId;
	}

	protected IndexServiceClient getClient() {
		return this.config.getIndexServiceClient();
	}

	/**
	 * Get all index items created by this index provider.
	 * 
	 * @return
	 */
	@Override
	public List<IndexItemConfigurable> getIndexItems() throws IOException {
		return doGetIndexItems(this.indexProviderId, null);
	}

	/**
	 * Get index items created by this index provider with specified namespace.
	 * 
	 * @param namespace
	 * @return
	 */
	@Override
	public List<IndexItemConfigurable> getIndexItems(String namespace) throws IOException {
		return doGetIndexItems(this.indexProviderId, namespace);
	}

	/**
	 * 
	 * @param indexProviderId
	 * @param namespace
	 * @return
	 * @throws IOException
	 */
	protected List<IndexItemConfigurable> doGetIndexItems(String indexProviderId, String namespace) throws IOException {
		List<IndexItemConfigurable> indexItemConfigurables = new ArrayList<IndexItemConfigurable>();
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

				IndexItemConfigurable indexItemConfigurable = new IndexItemConfigurableImpl(this.config, currIndexProviderId, currNamespace, currName);
				indexItemConfigurables.add(indexItemConfigurable);
			}
		} catch (ClientException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		return indexItemConfigurables;
	}

	@Override
	public IndexItemConfigurable addIndexItem(String namespace, String name, Map<String, Object> properties) throws IOException {
		IndexItemDTO newIndexItemDTO = null;
		try {
			newIndexItemDTO = getClient().addIndexItem(this.indexProviderId, namespace, name, properties);
		} catch (ClientException e) {
			e.printStackTrace();
			throw new IOException(e);
		}

		if (newIndexItemDTO == null) {
			throw new IOException("Cannot get new index item response (IndexItemDTO) from web service call for adding new index item.");
		}

		String currIndexProviderId = newIndexItemDTO.getIndexProviderId();
		String currNamespace = newIndexItemDTO.getNamespace();
		String currName = newIndexItemDTO.getName();

		if (this.indexProviderId != null && !indexProviderId.equals(currIndexProviderId)) {
			System.err.println("The new IndexItemDTO '" + newIndexItemDTO.toString() + "' has a different indexProviderId ('" + currIndexProviderId + "') than the specified indexProviderId ('" + this.indexProviderId + "').");
		}
		if (namespace != null && !namespace.equals(currNamespace)) {
			System.err.println("The new IndexItemDTO '" + newIndexItemDTO.toString() + "' has a different namespace ('" + currNamespace + "') than the specified namespace  ('" + namespace + "').");
		}
		if (name != null && !name.equals(currName)) {
			System.err.println("The new IndexItemDTO '" + newIndexItemDTO.toString() + "' has a different name ('" + currName + "') than the specified name  ('" + name + "').");
		}

		IndexItemConfigurable indexItemConfigurable = new IndexItemConfigurableImpl(this.config, currIndexProviderId, currNamespace, currName);
		return indexItemConfigurable;
	}

}
