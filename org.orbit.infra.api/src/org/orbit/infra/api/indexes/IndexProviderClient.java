package org.orbit.infra.api.indexes;

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
public interface IndexProviderClient extends IndexServiceClient {

}
