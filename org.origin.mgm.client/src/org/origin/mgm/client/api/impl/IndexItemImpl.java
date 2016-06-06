package org.origin.mgm.client.api.impl;

import java.util.Map;

import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexServiceConfiguration;
import org.origin.mgm.client.ws.IndexServiceClient;

public class IndexItemImpl implements IndexItem {

	protected IndexServiceConfiguration config;
	protected String indexProviderId;
	protected String namespace;
	protected String name;

	/**
	 * 
	 * @param config
	 * @param indexProviderId
	 * @param namespace
	 * @param name
	 */
	public IndexItemImpl(IndexServiceConfiguration config, String indexProviderId, String namespace, String name) {
		this.config = config;
		this.indexProviderId = indexProviderId;
		this.namespace = namespace;
		this.name = name;
	}

	protected IndexServiceClient getClient() {
		return this.config.getIndexServiceClient();
	}

	@Override
	public String getIndexProviderId() {
		return this.indexProviderId;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getName() {
		return name;
	}

	@Override
	public Map<String, Object> getProperties() {
		// TODO:
		// Call web service client
		return null;
	}

}
