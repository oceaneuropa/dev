package org.origin.mgm.client.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.adapter.IAdaptable;
import org.origin.mgm.client.api.impl.IndexProviderImpl;

public abstract class IndexProvider implements IAdaptable {

	/**
	 * 
	 * @param config
	 * @param indexProviderId
	 * @return
	 */
	public static IndexProvider newInstance(IndexServiceConfiguration config, String indexProviderId) {
		return new IndexProviderImpl(config, indexProviderId);
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
	 * Get index items created by this index provider with specified tns.
	 * 
	 * @param namespace
	 * @return
	 */
	public abstract List<IndexItemConfigurable> getIndexItems(String namespace) throws IOException;

	/**
	 * Create a index item.
	 * 
	 * @param namespace
	 * @param name
	 * @return
	 */
	public abstract IndexItemConfigurable createIndexItem(String namespace, String name) throws IOException;

	/**
	 * Create a index item with properties.
	 * 
	 * @param namespace
	 * @param name
	 * @param properties
	 * @return
	 */
	public abstract IndexItemConfigurable createIndexItem(String namespace, String name, Map<String, Object> properties) throws IOException;

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
