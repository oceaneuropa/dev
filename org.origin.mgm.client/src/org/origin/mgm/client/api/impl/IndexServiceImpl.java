package org.origin.mgm.client.api.impl;

import java.util.List;

import org.origin.common.rest.client.ClientConfiguration;
import org.origin.mgm.client.api.IndexService;
import org.origin.mgm.client.ws.IndexServiceClient;
import org.origin.mgm.model.runtime.IndexItem;

public class IndexServiceImpl implements IndexService {

	protected ClientConfiguration clientConfig;
	protected IndexServiceClient indexServiceClient;

	/**
	 * 
	 * @param url
	 * @param contextRoot
	 * @param username
	 * @param password
	 */
	public IndexServiceImpl(String url, String contextRoot, String username, String password) {
		this.clientConfig = ClientConfiguration.get(url, contextRoot, username, password);

		// ws client for IndexService
		this.indexServiceClient = new IndexServiceClient(this.clientConfig);
	}

	@Override
	public List<IndexItem> getIndexItems() {
		return null;
	}

	@Override
	public boolean isIndexed(String type, String name) {
		return false;
	}

	@Override
	public void addIndexItem(String type, String name) {

	}

	@Override
	public void removeIndexItem(String type, String name) {

	}

	@Override
	public boolean hasProperty(String type, String name, String propName) {
		return false;
	}

	@Override
	public void setProperty(String type, String name, String propName, String propValue) {

	}

	@Override
	public void setVolatileProperty(String type, String name, String propName, String propValue) {

	}

	@Override
	public void removeProperty(String type, String name, String propName) {

	}

}
