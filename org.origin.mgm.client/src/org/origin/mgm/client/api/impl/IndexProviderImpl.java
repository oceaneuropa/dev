package org.origin.mgm.client.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.mgm.client.api.IndexItemUpdatable;
import org.origin.mgm.client.api.IndexProvider;
import org.origin.mgm.client.api.IndexServiceConfiguration;
import org.origin.mgm.model.dto.IndexItemDTO;

public class IndexProviderImpl extends IndexServiceImpl implements IndexProvider {

	protected IndexServiceConfiguration config;

	/**
	 * 
	 * @param indexProviderId
	 * @param config
	 */
	public IndexProviderImpl(IndexServiceConfiguration config) {
		super(config);
	}

	@Override
	public List<IndexItemUpdatable> getUpdatableIndexItems(String indexProviderId) throws IOException {
		return doUpdatableGetIndexItems(indexProviderId, null);
	}

	/**
	 * Get index items created by this index provider with specified type.
	 * 
	 * @param type
	 * @return
	 */
	@Override
	public List<IndexItemUpdatable> getUpdatableIndexItems(String indexProviderId, String type) throws IOException {
		return doUpdatableGetIndexItems(indexProviderId, type);
	}

	/**
	 * 
	 * @param indexProviderId
	 * @param type
	 * @return
	 * @throws IOException
	 */
	protected List<IndexItemUpdatable> doUpdatableGetIndexItems(String indexProviderId, String type) throws IOException {
		List<IndexItemUpdatable> indexItemConfigurables = new ArrayList<IndexItemUpdatable>();
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

				IndexItemUpdatable indexItemUpdatable = new IndexItemUpdatableImpl(this.config, currIndexProviderId, currType, currName, currProperties);
				indexItemConfigurables.add(indexItemUpdatable);
			}
		} catch (ClientException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		return indexItemConfigurables;
	}

	@Override
	public IndexItemUpdatable addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException {
		IndexItemUpdatable newIndexItemUpdatable = null;
		try {
			IndexItemDTO newIndexItemDTO = getClient().addIndexItem(indexProviderId, type, name, properties);
			if (newIndexItemDTO != null) {

				String currIndexProviderId = newIndexItemDTO.getIndexProviderId();
				String currType = newIndexItemDTO.getType();
				String currName = newIndexItemDTO.getName();
				Map<String, Object> currProperties = newIndexItemDTO.getProperties();

				newIndexItemUpdatable = new IndexItemUpdatableImpl(this.config, currIndexProviderId, currType, currName, currProperties);
			}
		} catch (ClientException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		return newIndexItemUpdatable;
	}

	@Override
	public boolean removeIndexItem(Integer indexItemId) throws IOException {
		try {
			StatusDTO status = getClient().removeIndexItem(indexItemId);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean removeIndexItem(String indexProviderId, String type, String name) throws IOException {
		try {
			StatusDTO status = getClient().removeIndexItem(indexProviderId, type, name);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return false;
	}

}
