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
