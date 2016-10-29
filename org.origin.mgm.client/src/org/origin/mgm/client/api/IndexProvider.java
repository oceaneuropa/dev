package org.origin.mgm.client.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/*
 * Used by index provider to create/update/delete index items.
 * 
 * Note:
 * indexProviderId should never appear in the method parameters, since the IndexProvider already holds a indexProviderId when created.
 * 
 * Example:
 * 
 * IndexService: "component.indexservice.indexer"
 * 
 * AppStore: "component.appstore.indexer"
 * 
 * ConfigRegisry: "component.configregistry.indexer"
 * 
 * FileSystem: "component.filesystem.indexer"
 * 
 */
public interface IndexProvider extends IndexService {

	/**
	 * Get updatable index items.
	 * 
	 * @param indexProviderId
	 * @return
	 */
	public List<IndexItemUpdatable> getUpdatableIndexItems(String indexProviderId) throws IOException;

	/**
	 * Get updatable index items.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @return
	 */
	public List<IndexItemUpdatable> getUpdatableIndexItems(String indexProviderId, String type) throws IOException;

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
	public IndexItemUpdatable addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException;

	/**
	 * Remove an index item.
	 * 
	 * @param indexItemId
	 * @return
	 * @throws IOException
	 */
	public boolean removeIndexItem(Integer indexItemId) throws IOException;

	/**
	 * Remove an index item.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public boolean removeIndexItem(String indexProviderId, String type, String name) throws IOException;

}
