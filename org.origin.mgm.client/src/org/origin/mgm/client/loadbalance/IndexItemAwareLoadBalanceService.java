package org.origin.mgm.client.loadbalance;

import java.util.Hashtable;
import java.util.Map;

import org.origin.common.loadbalance.AbstractLoadBalanceService;
import org.origin.mgm.client.api.IndexItem;

public class IndexItemAwareLoadBalanceService<S> extends AbstractLoadBalanceService<S> {

	protected IndexItem indexItem;

	/**
	 * 
	 * @param service
	 * @param indexItem
	 */
	public IndexItemAwareLoadBalanceService(S service, IndexItem indexItem) {
		super(service);
		if (indexItem == null) {
			throw new IllegalArgumentException("indexItem is null.");
		}
		this.indexItem = indexItem;
		this.setId(String.valueOf(this.indexItem.getIndexItemId()));
	}

	public IndexItem getIndexItem() {
		return this.indexItem;
	}

	public void setIndexItem(IndexItem indexItem) {
		if (indexItem == null) {
			throw new IllegalArgumentException("indexItem is null.");
		}
		this.indexItem = indexItem;
	}

	@Override
	public synchronized Map<String, Object> getProperties() {
		Map<String, Object> resultProperties = new Hashtable<String, Object>();

		Map<String, Object> superProperties = super.getProperties();
		if (superProperties != null && !superProperties.isEmpty()) {
			resultProperties.putAll(superProperties);
		}

		Map<String, Object> indexItemProperties = this.indexItem.getProperties();
		if (indexItemProperties != null && !indexItemProperties.isEmpty()) {
			resultProperties.putAll(indexItemProperties);
		}

		return resultProperties;
	}

	@Override
	public synchronized boolean hasProperty(String key) {
		if (this.indexItem.getProperties().containsKey(key)) {
			return true;
		}

		if (super.hasProperty(key)) {
			return true;
		}

		return false;
	}

	@Override
	public synchronized Object getProperty(String key) {
		Object indexItemPropertyValue = this.indexItem.getProperties().get(key);
		if (indexItemPropertyValue != null) {
			return indexItemPropertyValue;
		}

		Object superPropertyValue = super.getProperty(key);
		if (superPropertyValue != null) {
			return superPropertyValue;
		}

		return null;
	}

}
