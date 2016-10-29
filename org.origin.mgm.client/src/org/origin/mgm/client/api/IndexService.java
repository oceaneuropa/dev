package org.origin.mgm.client.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.origin.common.adapter.IAdaptable;

/**
 * Used by client to search index items.
 *
 */
public interface IndexService extends IAdaptable {

	/**
	 * Get remote index service server configuration
	 * 
	 * @return
	 */
	public abstract IndexServiceConfiguration getConfiguration();

	/**
	 * Ping the index service. Use integer as return value to allow more status of the server.
	 * 
	 * @return result larger than 0 means service is available. result equals or smaller than 0 means service is not available.
	 */
	public abstract int ping();

	/**
	 * Get all index items.
	 * 
	 * @return
	 */
	public abstract List<IndexItem> getIndexItems() throws IOException;

	/**
	 * Get all index items created by specified indexer provider.
	 * 
	 * @param indexProviderId
	 * @return
	 * @throws IOException
	 */
	public abstract List<IndexItem> getIndexItems(String indexProviderId) throws IOException;

	/**
	 * Get index items created by specified indexer provider and with specified type.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public abstract List<IndexItem> getIndexItems(String indexProviderId, String type) throws IOException;

	/**
	 * Execute an action with optional parameters.
	 * 
	 * @param action
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public abstract boolean sendCommand(String action, Map<String, Object> params) throws IOException;

}
