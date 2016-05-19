package org.origin.mgm.client.api.impl;

import java.io.IOException;
import java.util.List;

import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexService;
import org.origin.mgm.client.api.IndexServiceConfiguration;

public class IndexServiceImpl extends IndexService {

	protected IndexServiceConfiguration config;

	/**
	 * 
	 * @param url
	 * @param contextRoot
	 * @param username
	 * @param password
	 */
	public IndexServiceImpl(IndexServiceConfiguration config) {
		this.config = config;
	}

	@Override
	public IndexServiceConfiguration getConfiguration() {
		return this.config;
	}

	@Override
	public List<IndexItem> getIndexItems() {
		return null;
	}

	@Override
	public List<IndexItem> getIndexItems(String namespace) throws IOException {
		return null;
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId, String namespace) throws IOException {
		return null;
	}

}
