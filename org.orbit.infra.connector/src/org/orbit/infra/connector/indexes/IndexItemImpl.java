package org.orbit.infra.connector.indexes;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;

public class IndexItemImpl implements IndexItem {

	protected Integer indexItemId;
	protected String indexProviderId;
	protected String type;
	protected String name;
	protected Map<String, Object> properties;

	/**
	 * 
	 * @param indexItemId
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param properties
	 */
	public IndexItemImpl(Integer indexItemId, String indexProviderId, String type, String name, Map<String, Object> properties) {
		this.indexItemId = indexItemId;
		this.indexProviderId = indexProviderId;
		this.type = type;
		this.name = name;
		this.properties = properties;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((indexItemId == null) ? 0 : indexItemId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		IndexItemImpl other = (IndexItemImpl) obj;
		if (indexItemId == null) {
			if (other.indexItemId != null) {
				return false;
			}
		} else if (!indexItemId.equals(other.indexItemId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "IndexItemImpl [indexItemId=" + indexItemId + ", indexProviderId=" + indexProviderId + ", type=" + type + ", name=" + name + "]";
	}

}

/// **
// *
// * @param config
// * @param indexItemId
// * @param indexProviderId
// * @param type
// * @param name
// * @param properties
// */
// public IndexItemImpl(IndexServiceConfiguration config, Integer indexItemId, String indexProviderId, String type, String name, Map<String, Object> properties)
/// {
// this.config = config;
// this.indexItemId = indexItemId;
// this.indexProviderId = indexProviderId;
// this.type = type;
// this.name = name;
// this.properties = properties;
// }
//
// protected IndexServiceWSClient getClient() {
// return this.config.getClient();
// }
