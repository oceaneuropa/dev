package org.orbit.infra.runtime.indexes.service;

import java.util.List;
import java.util.Map;

import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.model.indexes.IndexServiceException;
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
	 * @throws IndexServiceException
	 */
	public List<IndexItem> getIndexItems(String indexProviderId) throws IndexServiceException;

	/**
	 * Get index items with specified type and created by specified indexer provider.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @return
	 * @throws IndexServiceException
	 */
	public List<IndexItem> getIndexItems(String indexProviderId, String type) throws IndexServiceException;

	/**
	 * Get index item.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @return
	 * @throws IndexServiceException
	 */
	public IndexItem getIndexItem(String indexProviderId, String type, String name) throws IndexServiceException;

	/**
	 * Get index item.
	 * 
	 * @param indexItemId
	 * @return
	 * @throws IndexServiceException
	 */
	public IndexItem getIndexItem(String indexProviderId, Integer indexItemId) throws IndexServiceException;

	/**
	 * Add an index item.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param properties
	 * @return
	 * @throws IndexServiceException
	 */
	public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IndexServiceException;

	/**
	 * Remove an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @return
	 * @throws IndexServiceException
	 */
	public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws IndexServiceException;

	/**
	 * Check whether a property of an index item exists.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param propName
	 * @return
	 * @throws IndexServiceException
	 */
	public boolean hasProperty(String indexProviderId, Integer indexItemId, String propName) throws IndexServiceException;

	/**
	 * Get the values of all properties of an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @return
	 * @throws IndexServiceException
	 */
	public Map<String, Object> getProperties(String indexProviderId, Integer indexItemId) throws IndexServiceException;

	/**
	 * Get the property value of an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param propName
	 * @return
	 * @throws IndexServiceException
	 */
	public Object getProperty(String indexProviderId, Integer indexItemId, String propName) throws IndexServiceException;

	/**
	 * Set the properties of an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param properties
	 * @return
	 * @throws IndexServiceException
	 */
	public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws IndexServiceException;

	/**
	 * Remove the properties of an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param propNames
	 * @return
	 * @throws IndexServiceException
	 */
	public boolean removeProperty(String indexProviderId, Integer indexItemId, List<String> propNames) throws IndexServiceException;

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
