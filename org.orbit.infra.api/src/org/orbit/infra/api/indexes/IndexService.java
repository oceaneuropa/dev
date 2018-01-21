package org.orbit.infra.api.indexes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// import org.orbit.component.connector.tier0.indexes.IndexServiceConfiguration;
import org.origin.common.adapter.IAdaptable;
import org.origin.common.service.ProxyService;

/**
 * Used by client to query index items.
 *
 */
public interface IndexService extends ProxyService, IAdaptable {

	String getName();

	String getURL();

	Map<String, Object> getProperties();

	// void update(Map<Object, Object> properties);

	/**
	 * Ping the index service. Use integer as return value to allow more status of the server.
	 * 
	 * @return result larger than 0 means service is available. result equals or smaller than 0 means service is not available.
	 */
	boolean ping();

	/**
	 * Execute an action with optional parameters.
	 * 
	 * @param action
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public boolean sendCommand(String action, Map<String, Object> params) throws IOException;

	/**
	 * Get all index items.
	 * 
	 * @return
	 */
	List<IndexItem> getIndexItems() throws IOException;

	/**
	 * Get all index items created by specified indexer provider.
	 * 
	 * @param indexProviderId
	 * @return
	 * @throws IOException
	 */
	public List<IndexItem> getIndexItems(String indexProviderId) throws IOException;

	/**
	 * Get index items created by specified indexer provider and with specified type.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public List<IndexItem> getIndexItems(String indexProviderId, String type) throws IOException;

	/**
	 * Get an index item.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public IndexItem getIndexItem(String indexProviderId, String type, String name) throws IOException;

	/**
	 * Get an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @return
	 * @throws IOException
	 */
	public IndexItem getIndexItem(String indexProviderId, Integer indexItemId) throws IOException;

}

/// **
// * Whether whether an index item exists.
// *
// * @param indexProviderId
// * @param type
// * @param name
// * @return
// * @throws IOException
// */
// public boolean hasIndexItem(String indexProviderId, String type, String name) throws IOException;

/// **
// * Whether whether an index item exists.
// *
// * @param indexItemId
// * @return
// * @throws IOException
// */
// public boolean hasIndexItem(Integer indexItemId) throws IOException;
