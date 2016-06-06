package org.origin.mgm.client.api.impl;

import java.util.Map;

import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexItemConfigurable;
import org.origin.mgm.client.api.IndexServiceConfiguration;

public class IndexItemConfigurableImpl extends IndexItemImpl implements IndexItem, IndexItemConfigurable {

	/**
	 * 
	 * @param config
	 * @param indexProviderId
	 * @param namespace
	 * @param name
	 */
	public IndexItemConfigurableImpl(IndexServiceConfiguration config, String indexProviderId, String namespace, String name) {
		super(config, indexProviderId, namespace, name);
	}

	/** implement IndexItemConfigurable interface */
	@Override
	public void update(Map<String, Object> properties) {
		// TODO:
		// Call web service client
	}

	@Override
	public void setProperty(String propName, Object propValue) {
		// TODO:
		// Call web service client
	}

	@Override
	public void setProperty(String propName, Object propValue, boolean isVolatile) {
		// TODO:
		// Call web service client
	}

	@Override
	public void removeProperty(String propName) {
		// TODO:
		// Call web service client
	}

	@Override
	public void delete() {
		// TODO:
		// Call web service client
	}

}
