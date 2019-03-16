package org.orbit.infra.api.indexes;

import java.util.Map;

public interface IndexItemUpdater {

	/**
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param properties
	 */
	void addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties);

	/**
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param properties
	 */
	void setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties);

}
