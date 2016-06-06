package org.origin.mgm.client.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.adapter.IAdaptable;
import org.origin.mgm.client.api.impl.IndexProviderImpl;

/*
 * Used by index provider to create/update/delete index items.
 * 
 * Note: 
 * indexProviderId should never appear in the method parameters, since the IndexProvider already holds a indexProviderId when created.
 * 
 */
public abstract class IndexProvider implements IAdaptable {

	/**
	 * 
	 * @param indexProviderId
	 * @param config
	 * @return
	 */
	public static IndexProvider newInstance(String indexProviderId, IndexServiceConfiguration config) {
		return new IndexProviderImpl(indexProviderId, config);
	}

	private AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * Get remote index service server configuration
	 * 
	 * @return
	 */
	public abstract IndexServiceConfiguration getConfiguration();

	/**
	 * Get index provider id.
	 * 
	 * @return
	 */
	public abstract String getIndexProviderId();

	/**
	 * Get all index items created by this index provider.
	 * 
	 * @return
	 */
	public abstract List<IndexItemConfigurable> getIndexItems() throws IOException;

	/**
	 * Get index items created by this index provider with specified namespace.
	 * 
	 * @param namespace
	 * @return
	 */
	public abstract List<IndexItemConfigurable> getIndexItems(String namespace) throws IOException;

	/**
	 * Add an index item.
	 * 
	 * @param namespace
	 * @param name
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	public abstract IndexItemConfigurable addIndexItem(String namespace, String name, Map<String, Object> properties) throws IOException;

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
