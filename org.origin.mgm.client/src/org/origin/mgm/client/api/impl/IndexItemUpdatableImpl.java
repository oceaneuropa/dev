package org.origin.mgm.client.api.impl;

import java.util.Map;

import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexItemUpdatable;
import org.origin.mgm.client.api.IndexServiceConfiguration;

public class IndexItemUpdatableImpl extends IndexItemImpl implements IndexItem, IndexItemUpdatable {

	/**
	 * 
	 * @param config
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param properties
	 */
	public IndexItemUpdatableImpl(IndexServiceConfiguration config, String indexProviderId, String type, String name, Map<String, Object> properties) {
		super(config, indexProviderId, type, name, properties);
	}

	/** implement IndexItemUpdatable interface */
	@Override
	public boolean setProperties(Map<String, Object> properties) {
		// Call web service client
		return false;
	}

	@Override
	public boolean setProperty(String propName, Object propValue) {
		// Call web service client
		return false;
	}

	@Override
	public boolean setProperty(String propName, Object propValue, boolean isVolatile) {
		// Call web service client
		return false;
	}

	@Override
	public boolean removeProperty(String propName) {
		// Call web service client
		return false;
	}

	@Override
	public boolean delete() {
		// Call web service client
		return false;
	}

}
