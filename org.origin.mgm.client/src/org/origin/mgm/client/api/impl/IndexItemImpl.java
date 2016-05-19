package org.origin.mgm.client.api.impl;

import java.util.Map;

import javax.xml.namespace.QName;

import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexServiceConfiguration;
import org.origin.mgm.client.ws.IndexServiceClient;

public class IndexItemImpl implements IndexItem {

	protected IndexServiceConfiguration config;
	protected String indexProviderId;
	protected QName qName;

	/**
	 * 
	 * @param config
	 * @param indexProviderId
	 * @param qName
	 */
	public IndexItemImpl(IndexServiceConfiguration config, String indexProviderId, QName qName) {
		this.config = config;
		this.indexProviderId = indexProviderId;
		this.qName = qName;
	}

	protected IndexServiceClient getClient() {
		return this.config.getIndexServiceClient();
	}

	@Override
	public String getIndexProviderId() {
		return this.indexProviderId;
	}

	@Override
	public QName getQName() {
		return this.qName;
	}

	@Override
	public Map<String, Object> getProperties() {
		return null;
	}

}
