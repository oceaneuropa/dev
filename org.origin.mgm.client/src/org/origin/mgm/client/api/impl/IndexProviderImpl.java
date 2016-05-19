package org.origin.mgm.client.api.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.origin.mgm.client.api.IndexItemConfigurable;
import org.origin.mgm.client.api.IndexProvider;
import org.origin.mgm.client.api.IndexServiceConfiguration;
import org.origin.mgm.client.ws.IndexServiceClient;

public class IndexProviderImpl extends IndexProvider {

	protected IndexServiceConfiguration config;
	protected String indexProviderId;

	/**
	 * 
	 * @param config
	 * @param indexProviderId
	 */
	public IndexProviderImpl(IndexServiceConfiguration config, String indexProviderId) {
		this.config = config;
		this.indexProviderId = indexProviderId;
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

	@Override
	public List<IndexItemConfigurable> getIndexItems() throws IOException {
		return getIndexItems(null);
	}

	@Override
	public List<IndexItemConfigurable> getIndexItems(String namespace) throws IOException {
		return null;
	}

	@Override
	public IndexItemConfigurable createIndexItem(String namespace, String name) throws IOException {
		return null;
	}

	@Override
	public IndexItemConfigurable createIndexItem(String namespace, String name, Map<String, Object> properties) throws IOException {
		return null;
	}

}
