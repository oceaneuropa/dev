package org.origin.mgm.service;

import java.util.List;
import java.util.Map;

import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.runtime.IndexItem;

public interface IndexService {

	/**
	 * Get the index service configuration.
	 * 
	 * @return
	 */
	public IndexServiceConfiguration getConfiguration();

	/**
	 * Get all index items with any type and created by any index provider.
	 * 
	 * @return
	 * @throws IndexServiceException
	 */
	public List<IndexItem> getIndexItems() throws IndexServiceException;

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
	 * @param indexItemId
	 * @return
	 * @throws IndexServiceException
	 */
	public IndexItem getIndexItem(Integer indexItemId) throws IndexServiceException;

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
	 * Add an index item.
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param properties
	 * @return
	 * @throws IndexServiceException
	 */
	public boolean addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IndexServiceException;

	/**
	 * Remove an index item.
	 * 
	 * @param indexItemId
	 * @return
	 * @throws IndexServiceException
	 */
	public boolean removeIndexItem(Integer indexItemId) throws IndexServiceException;

	/**
	 * Check whether a property of an index item exists.
	 * 
	 * @param indexItemId
	 * @param propName
	 * @return
	 * @throws IndexServiceException
	 */
	public boolean hasProperty(Integer indexItemId, String propName) throws IndexServiceException;

	/**
	 * Get the values of all properties of an index item.
	 * 
	 * @param indexItemId
	 * @return
	 * @throws IndexServiceException
	 */
	public Map<String, ?> getProperties(Integer indexItemId) throws IndexServiceException;

	/**
	 * Get the property value of an index item.
	 * 
	 * @param indexItemId
	 * @param propName
	 * @return
	 * @throws IndexServiceException
	 */
	public Map<String, ?> getProperty(Integer indexItemId, String propName) throws IndexServiceException;

	/**
	 * Set the properties of an index item.
	 * 
	 * @param indexItemId
	 * @param properties
	 * @throws IndexServiceException
	 */
	public boolean setProperty(Integer indexItemId, Map<String, Object> properties) throws IndexServiceException;

	/**
	 * Remove the properties of an index item.
	 * 
	 * @param indexItemId
	 * @param propNames
	 * @return
	 * @throws IndexServiceException
	 */
	public boolean removeProperty(Integer indexItemId, List<String> propNames) throws IndexServiceException;

	/**
	 * Add a IndexServiceListener.
	 * 
	 * @param listener
	 */
	public void addListener(IndexServiceListener listener);

	/**
	 * Remove a IndexServiceListener.
	 * 
	 * @param listener
	 */
	public void removeListener(IndexServiceListener listener);

}
