package org.origin.mgm.client.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.adapter.IAdaptable;
import org.origin.mgm.client.api.impl.IndexServiceImpl;

/**
 * Used by client to search index items.
 *
 */
public abstract class IndexService implements IAdaptable {

	/**
	 * 
	 * @param config
	 * @param indexProviderId
	 * @return
	 */
	public static IndexService newInstance(IndexServiceConfiguration config) {
		return new IndexServiceImpl(config);
	}

	private AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * Get remote index service server configuration
	 * 
	 * @return
	 */
	public abstract IndexServiceConfiguration getConfiguration();

	/**
	 * Get all index items.
	 * 
	 * @return
	 */
	public abstract List<IndexItem> getIndexItems() throws IOException;

	/**
	 * Get index items, which are created by any index provider, with specified namespace.
	 * 
	 * @param namespace
	 * @return
	 * @throws IOException
	 */
	public abstract List<IndexItem> getIndexItemsByNamespace(String namespace) throws IOException;

	/**
	 * Get all index items created by specified indexer provider.
	 * 
	 * @param indexProviderId
	 * @return
	 * @throws IOException
	 */
	public abstract List<IndexItem> getIndexItemsByIndexProvider(String indexProviderId) throws IOException;

	/**
	 * Get index items created by specified indexer provider and with specified namespace.
	 * 
	 * @param indexProviderId
	 * @param namespace
	 * @return
	 * @throws IOException
	 */
	public abstract List<IndexItem> getIndexItems(String indexProviderId, String namespace) throws IOException;

	/**
	 * Execute an action with optional parameters.
	 * 
	 * @param action
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public abstract boolean sendCommand(String action, Map<String, Object> parameters) throws IOException;

	/** implement IAdaptable interface */
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

}
