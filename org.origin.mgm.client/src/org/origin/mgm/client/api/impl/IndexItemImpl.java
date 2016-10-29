package org.origin.mgm.client.api.impl;

import java.util.Hashtable;
import java.util.Map;

import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexServiceConfiguration;
import org.origin.mgm.client.ws.IndexServiceClient;

public class IndexItemImpl implements IndexItem {

	protected IndexServiceConfiguration config;
	protected String indexProviderId;
	protected String type;
	protected String name;
	protected Map<String, Object> properties;

	/**
	 * 
	 * @param config
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param properties
	 */
	public IndexItemImpl(IndexServiceConfiguration config, String indexProviderId, String type, String name, Map<String, Object> properties) {
		this.config = config;
		this.indexProviderId = indexProviderId;
		this.type = type;
		this.name = name;
		this.properties = properties;
	}

	protected IndexServiceClient getClient() {
		return this.config.getIndexServiceClient();
	}

	@Override
	public String getIndexProviderId() {
		return this.indexProviderId;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public synchronized Map<String, Object> getProperties() {
		if (this.properties == null) {
			this.properties = new Hashtable<String, Object>();
		}
		return this.properties;
	}

}
