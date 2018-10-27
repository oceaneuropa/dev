package org.orbit.infra.api.indexes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.origin.common.rest.client.ServiceClient;

/**
 * Used by client to query index items.
 *
 */
public interface IndexServiceClient extends ServiceClient {

	/**
	 * Get all index items created by specified indexer provider.
	 * 
	 * @param indexProviderId
	 * @return
	 * @throws IOException
	 */
	List<IndexItem> getIndexItems(String indexProviderId) throws IOException;

	/**
	 * Get index items created by specified indexer provider and with specified type.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @return
	 * @throws IOException
	 */
	List<IndexItem> getIndexItems(String indexProviderId, String type) throws IOException;

	/**
	 * Get an index item.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @return
	 * @throws IOException
	 */
	IndexItem getIndexItem(String indexProviderId, String type, String name) throws IOException;

	/**
	 * Get an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @return
	 * @throws IOException
	 */
	IndexItem getIndexItem(String indexProviderId, Integer indexItemId) throws IOException;

	/**
	 * Add an index item.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException;

	/**
	 * Remove an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @return
	 * @throws IOException
	 */
	boolean deleteIndexItem(String indexProviderId, Integer indexItemId) throws IOException;

	/**
	 * Set properties.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws IOException;

	/**
	 * Set property.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param propName
	 * @param propValue
	 * @param propType
	 * @return
	 * @throws IOException
	 */
	boolean setProperty(String indexProviderId, Integer indexItemId, String propName, Object propValue, String propType) throws IOException;

	/**
	 * Remove property.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param propertyNames
	 * @return
	 * @throws IOException
	 */
	boolean removeProperties(String indexProviderId, Integer indexItemId, List<String> propertyNames) throws IOException;

}