package org.orbit.infra.runtime.indexes.service;

import java.util.List;
import java.util.Map;

import org.orbit.infra.model.indexes.IndexItem;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.WebServiceAware;

public interface IndexService extends WebServiceAware {

	public String getName();

	public String getHostURL();

	public String getContextRoot();

	/**
	 * Get index items with specified indexProviderId.
	 * 
	 * @param indexProviderId
	 * @return
	 * @throws ServerException
	 */
	public List<IndexItem> getIndexItems(String indexProviderId) throws ServerException;

	/**
	 * Get index items with specified type and created by specified indexer provider.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @return
	 * @throws ServerException
	 */
	public List<IndexItem> getIndexItems(String indexProviderId, String type) throws ServerException;

	/**
	 * Get index item.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @return
	 * @throws ServerException
	 */
	public IndexItem getIndexItem(String indexProviderId, String type, String name) throws ServerException;

	/**
	 * Get index item.
	 * 
	 * @param indexItemId
	 * @return
	 * @throws ServerException
	 */
	public IndexItem getIndexItem(String indexProviderId, Integer indexItemId) throws ServerException;

	/**
	 * Add an index item.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param properties
	 * @return
	 * @throws ServerException
	 */
	public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws ServerException;

	/**
	 * Remove an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @return
	 * @throws ServerException
	 */
	public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws ServerException;

	/**
	 * Check whether a property of an index item exists.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param propName
	 * @return
	 * @throws ServerException
	 */
	public boolean hasProperty(String indexProviderId, Integer indexItemId, String propName) throws ServerException;

	/**
	 * Get the values of all properties of an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @return
	 * @throws ServerException
	 */
	public Map<String, Object> getProperties(String indexProviderId, Integer indexItemId) throws ServerException;

	/**
	 * Get the property value of an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param propName
	 * @return
	 * @throws ServerException
	 */
	public Object getProperty(String indexProviderId, Integer indexItemId, String propName) throws ServerException;

	/**
	 * Set the properties of an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param properties
	 * @return
	 * @throws ServerException
	 */
	public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws ServerException;

	/**
	 * Remove the properties of an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param propNames
	 * @return
	 * @throws ServerException
	 */
	public boolean removeProperties(String indexProviderId, Integer indexItemId, List<String> propNames) throws ServerException;

}

/// **
// * Get the index service configuration.
// *
// * @return
// */
// public IndexServiceConfiguration getConfiguration();

// /**
// * Add a IndexServiceListener.
// *
// * @param listener
// */
// public void addListener(IndexServiceListener listener);

// /**
// * Remove a IndexServiceListener.
// *
// * @param listener
// */
// public void removeListener(IndexServiceListener listener);
