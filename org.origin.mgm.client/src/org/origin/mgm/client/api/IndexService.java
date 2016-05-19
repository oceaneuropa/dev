package org.origin.mgm.client.api;

import java.io.IOException;
import java.util.List;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.adapter.IAdaptable;
import org.origin.mgm.client.api.impl.IndexServiceImpl;

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
	 * Get all index items with any namespace and created by any index provider.
	 * 
	 * @return
	 */
	public abstract List<IndexItem> getIndexItems() throws IOException;

	/**
	 * Get index items with specified namespace.
	 * 
	 * @return
	 */
	public abstract List<IndexItem> getIndexItems(String namespace) throws IOException;

	/**
	 * Get index items with specified namespace and created by specified indexer provider.
	 * 
	 * @return
	 */
	public abstract List<IndexItem> getIndexItems(String indexProviderId, String namespace) throws IOException;

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
