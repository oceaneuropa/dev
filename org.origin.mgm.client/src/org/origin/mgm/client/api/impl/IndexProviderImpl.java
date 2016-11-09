package org.origin.mgm.client.api.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.origin.common.json.JSONUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.mgm.client.api.IndexItem;
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
	public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException {
		IndexItem newIndexItem = null;
		try {
			IndexItemDTO indexItemDTO = getClient().addIndexItem(indexProviderId, type, name, properties);
			if (indexItemDTO != null) {
				Integer currIndexItemId = indexItemDTO.getIndexItemId();
				String currIndexProviderId = indexItemDTO.getIndexProviderId();
				String currType = indexItemDTO.getType();
				String currName = indexItemDTO.getName();

				// Map<String, Object> currProperties = newIndexItemDTO.getProperties();
				String propertiesString = indexItemDTO.getPropertiesString();
				Map<String, Object> currProperties = JSONUtil.toProperties(propertiesString);

				newIndexItem = new IndexItemImpl(this.config, currIndexItemId, currIndexProviderId, currType, currName, currProperties);
			}
		} catch (ClientException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		return newIndexItem;
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
			throw new IOException(e);
		}
		return false;
	}

	@Override
	public boolean setProperties(Integer indexItemId, Map<String, Object> properties) throws IOException {
		try {
			StatusDTO status = getClient().setProperties(indexItemId, properties);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		return false;
	}

	@Override
	public boolean setProperty(Integer indexItemId, String propName, Object propValue, String propType) throws IOException {
		try {
			StatusDTO status = getClient().setProperty(indexItemId, propName, propValue, propType);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		return false;
	}

	@Override
	public boolean removeProperties(Integer indexItemId, List<String> propertyNames) throws IOException {
		try {
			StatusDTO status = getClient().removeProperties(indexItemId, propertyNames);
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
