package org.origin.mgm.client.api.impl;

import java.util.Hashtable;
import java.util.Map;

import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexServiceConfiguration;
import org.origin.mgm.client.ws.IndexServiceWSClient;

public class IndexItemImpl implements IndexItem {

	protected IndexServiceConfiguration config;
	protected Integer indexItemId;
	protected String indexProviderId;
	protected String type;
	protected String name;
	protected Map<String, Object> properties;

	/**
	 * 
	 * @param config
	 * @param indexItemId
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param properties
	 */
	public IndexItemImpl(IndexServiceConfiguration config, Integer indexItemId, String indexProviderId, String type, String name, Map<String, Object> properties) {
		this.config = config;
		this.indexItemId = indexItemId;
		this.indexProviderId = indexProviderId;
		this.type = type;
		this.name = name;
		this.properties = properties;
	}

	protected IndexServiceWSClient getClient() {
		return this.config.getClient();
	}

	@Override
	public Integer getIndexItemId() {
		return this.indexItemId;
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
