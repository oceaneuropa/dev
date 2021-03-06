package other.orbit.infra.api.indexes;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.origin.common.loadbalance.LoadBalanceResourceImpl;

public class IndexItemAwareLoadBalanceResourceV1<S> extends LoadBalanceResourceImpl<S> {

	protected IndexItem indexItem;

	/**
	 * 
	 * @param service
	 * @param indexItem
	 */
	public IndexItemAwareLoadBalanceResourceV1(S service, IndexItem indexItem) {
		super(service);
		if (indexItem == null) {
			throw new IllegalArgumentException("indexItem is null.");
		}
		this.indexItem = indexItem;
		this.setId(this.indexItem.getIndexItemId().toString());
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
