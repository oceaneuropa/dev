package org.orbit.infra.api.indexes;

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
 * IndexService: "component_.index_service.indexer"
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
	 * Add an index item.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException;

	/**
	 * Remove an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @return
	 * @throws IOException
	 */
	public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws IOException;

	/**
	 * Set properties.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws IOException;

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
	public boolean setProperty(String indexProviderId, Integer indexItemId, String propName, Object propValue, String propType) throws IOException;

	/**
	 * Remove property.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param propertyNames
	 * @return
	 * @throws IOException
	 */
	public boolean removeProperties(String indexProviderId, Integer indexItemId, List<String> propertyNames) throws IOException;

}
